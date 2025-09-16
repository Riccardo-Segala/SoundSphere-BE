package backend.service;

import backend.dto.checkout.CheckoutInputRentalDTO;
import backend.dto.checkout.CheckoutOutputRentalDTO;
import backend.dto.noleggio.ResponseRentalDTO;
import backend.mapper.RentalMapper;
import backend.model.Noleggio;
import backend.model.*;
import backend.model.embeddable.NoleggioProdottoId;
import backend.repository.NoleggioRepository;
import backend.repository.UtenteRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class NoleggioService extends GenericService<Noleggio, UUID> {
    private final NoleggioRepository noleggioRepository;
    private final RentalMapper rentalMapper;

    public NoleggioService(NoleggioRepository noleggioRepository, RentalMapper rentalMapper) {
        super(noleggioRepository);
        this.noleggioRepository = noleggioRepository;
        this.rentalMapper = rentalMapper;// Passa il repository al costruttore della classe base
    }

    @Autowired
    private UtenteRepository utenteRepository;
    @Autowired
    private IndirizzoUtenteService indirizzoUtenteService;
    @Autowired
    private MetodoPagamentoService metodoPagamentoService;
    @Autowired
    private StockService stockService;
    @Autowired
    private CarrelloService carrelloService;
    @Autowired
    private DettagliNoleggioService dettagliNoleggioService;

    @Value("${app.filiale.online.name}")
    private String filialeOnlineName;


    @Transactional
    public CheckoutOutputRentalDTO checkoutNoleggio(CheckoutInputRentalDTO dto, UUID utenteId) {

        // --- 1. RECUPERO E VALIDAZIONE TRAMITE SERVICE ---
        // 1. Trova l'utente o lancia l'eccezione se non esiste.
        Utente utente = utenteRepository.findById(utenteId)
                .orElseThrow(() -> new EntityNotFoundException("Utente non trovato con ID: " + utenteId));

        IndirizzoUtente indirizzo = indirizzoUtenteService.findByIdAndValidateOwnership(dto.indirizzoSpedizioneId(), utenteId);
        MetodoPagamento metodoPagamento = metodoPagamentoService.findById(dto.metodoPagamentoId())
                .orElseThrow(() -> new EntityNotFoundException("Metodo di pagamento non valido"));

        List<Carrello> carrello = carrelloService.getCartByUtenteId(utenteId);
        if (carrello.isEmpty()) {
            throw new IllegalStateException("Impossibile procedere con il noleggio: il carrello è vuoto.");
        }

        // --- 2. CREA E SALVA IL NOLEGGIO "PADRE" PER PRIMO ---
        // Creiamo il noleggio con i dati specifici del noleggioDto.
        Noleggio nuovoNoleggio = new Noleggio();
        nuovoNoleggio.setUtente(utente);
        nuovoNoleggio.setDataInizio(dto.dataInizio());
        nuovoNoleggio.setDataScadenza(dto.dataFine());
        nuovoNoleggio.setIndirizzo(indirizzo);
        // Il totale verrà calcolato e impostato più avanti.
        // dataPagamento e dataRestituzione saranno null alla creazione.

        // Salva il noleggio per ottenere l'ID, fondamentale per i dettagli.
        Noleggio noleggioSalvato = noleggioRepository.save(nuovoNoleggio);

        // --- 3. LOGICA DI BUSINESS E CREAZIONE DEI DETTAGLI "FIGLI" ---
        // La logica qui cambia: invece di riservare lo stock, verifichiamo la disponibilità.
        List<DettagliNoleggio> dettagliNoleggio = carrello.stream()
                .map(cartElement -> {
                    Prodotto prodotto = cartElement.getProdotto();
                    stockService.reserveStockRental(filialeOnlineName, prodotto.getId(), cartElement.getQuantita());

                    DettagliNoleggio dettaglio = new DettagliNoleggio();

                    // Creiamo l'ID composto per la tabella dei dettagli noleggio
                    NoleggioProdottoId dettagliId = new NoleggioProdottoId(noleggioSalvato.getId(), prodotto.getId());
                    dettaglio.setId(dettagliId);

                    dettaglio.setNoleggio(noleggioSalvato);
                    dettaglio.setProdotto(prodotto);
                    dettaglio.setQuantita(cartElement.getQuantita());

                    return dettaglio;
                })
                .collect(Collectors.toList());
        dettagliNoleggioService.createByList(dettagliNoleggio);

        // --- 4. AGGIORNAMENTO FINALE E AZIONI POST-PERSISTENZA ---
        // **DIFFERENZA CHIAVE 2: Calcolo del totale**
        // Il totale non è una semplice somma, ma dipende dalla durata del noleggio.
        double totaleFinale = calcolaTotaleNoleggio(carrello, dto.dataInizio(), dto.dataFine());

        // Qui potresti inserire logiche aggiuntive, come un deposito cauzionale
        // o costi di servizio, in modo simile alla spedizione gratuita.

        noleggioSalvato.setTotale(totaleFinale);
        noleggioSalvato.setDataPagamento(LocalDate.now()); // Registriamo il pagamento

        noleggioSalvato.setDettagli(dettagliNoleggio);

        // Aggiorniamo l'entità 'managed'. JPA rileverà le modifiche.
        noleggioRepository.save(noleggioSalvato);

        // Svuotiamo il carrello, come prima
        carrelloService.deleteAllItems(carrello);
        // emailService.inviaConfermaNoleggio(noleggioSalvato, dettagliNoleggio);

        // --- 5. MAPPATURA DELLA RISPOSTA ---
        // Usiamo un mapper specifico per il noleggio per creare il DTO di risposta.
        return rentalMapper.toCheckoutOutputRentalDTO(noleggioSalvato);
    }

    // Metodo helper per il calcolo del totale
    private double calcolaTotaleNoleggio(List<Carrello> carrello, LocalDate inizio, LocalDate fine) {
        long giorniNoleggio = ChronoUnit.DAYS.between(inizio, fine);
        if (giorniNoleggio <= 0) {
            giorniNoleggio = 1; // Noleggio minimo di 1 giorno
        }

        double totaleParziale = 0.0;
        for (Carrello item : carrello) {
            // Logica di prezzo: prezzo giornaliero del prodotto * quantità * numero di giorni
            double prezzoGiornaliero = item.getProdotto().getCostoGiornaliero(); // Assumendo esista un prezzo di noleggio
            totaleParziale += (prezzoGiornaliero * item.getQuantita() * giorniNoleggio);
        }
        return totaleParziale;
    }

    // METODO PER ORGANIZZATORE EVENTI:
    // Restituisce la lista di tutti i noleggi per un singolo organizzatore
    public List<ResponseRentalDTO> findRentalsByUserId(UUID utenteId) {
        // 1. Usa il nuovo metodo del repository per trovare i noleggi
        List<Noleggio> noleggi = noleggioRepository.findByUtenteId(utenteId);

        // 2. Usa il mapper per convertire la lista di entità in una lista di DTO
        return noleggi.stream()
                .map(rentalMapper::toDto)
                .collect(Collectors.toList());
    }

    // METODO PER L'ADMIN:
    // Restituisce la lista di TUTTI i noleggi presenti nel sistema.
    public List<ResponseRentalDTO> findAllRentalsForAdmin() {
        // 1. Usa il metodo findAll() per recuperare tutti i noleggi
        List<Noleggio> tuttiINoleggi = noleggioRepository.findAll();

        // 2. Mappa il risultato in DTO
        return tuttiINoleggi.stream()
                .map(rentalMapper::toDto)
                .collect(Collectors.toList());
    }
}
