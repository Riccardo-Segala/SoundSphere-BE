package backend.service;

import backend.dto.checkout.CheckoutInputDTO;
import backend.dto.checkout.CheckoutOutputDTO;
import backend.dto.dati_statici.ResponseStaticDataDTO;
import backend.dto.ordine.ResponseOrderDTO;
import backend.mapper.OrderMapper;
import backend.model.*;
import backend.model.embeddable.OrdineProdottoId;
import backend.model.enums.StatoOrdine;
import backend.repository.OrdineRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrdineService extends GenericService<Ordine, UUID> {
    private final OrdineRepository ordineRepository;
    private final OrderMapper orderMapper;
    private final UtenteService utenteService;
    private final IndirizzoUtenteService indirizzoUtenteService;
    private final MetodoPagamentoService metodoPagamentoService;
    private final StockService stockService;
    private final CarrelloService carrelloService;
    private final DettagliOrdineService dettagliOrdineService;
    private final DatiStaticiService datiStaticiService;

    @Value("${app.filiale.online.name}")
    private String filialeOnlineName;

    public OrdineService(OrdineRepository repository,
                         OrdineRepository ordineRepository,
                         OrderMapper orderMapper,
                         UtenteService utenteService,
                         IndirizzoUtenteService indirizzoUtenteService,
                         MetodoPagamentoService metodoPagamentoService,
                         StockService stockService,
                         CarrelloService carrelloService,
                         DettagliOrdineService dettagliOrdineService,
                         DatiStaticiService datiStaticiService) {
        super(repository);
        this.ordineRepository = ordineRepository;
        this.orderMapper = orderMapper;
        this.utenteService = utenteService;
        this.indirizzoUtenteService = indirizzoUtenteService;
        this.metodoPagamentoService = metodoPagamentoService;
        this.stockService = stockService;
        this.carrelloService = carrelloService;
        this.dettagliOrdineService = dettagliOrdineService;
        this.datiStaticiService = datiStaticiService;
    }

    @Transactional
    public CheckoutOutputDTO checkout(CheckoutInputDTO checkoutDto, UUID utenteId) {

        // --- 1. RECUPERO E VALIDAZIONE TRAMITE SERVICE ---
        Utente utente = utenteService.findById(utenteId)
                .orElseThrow(() -> new EntityNotFoundException("Utente non valido"));
        IndirizzoUtente indirizzo = indirizzoUtenteService.findByIdAndValidateOwnership(checkoutDto.indirizzoSpedizioneId(), utenteId);
        MetodoPagamento metodoPagamento = metodoPagamentoService.findById(checkoutDto.metodoPagamentoId())
                .orElseThrow(() -> new EntityNotFoundException("Metodo di pagamento non valido"));

        List<Carrello> carrello = carrelloService.getCartByUtenteId(utenteId);
        if (carrello.isEmpty()) {
            throw new IllegalStateException("Impossibile procedere con il noleggio: il carrello è vuoto.");
        }

        // --- 2. CREA E SALVA L'ORDINE "PADRE" PER PRIMO ---
        // Creiamo l'ordine con i dati che già conosciamo. Il totale verrà aggiornato dopo.
        Ordine nuovoOrdine = new Ordine();
        nuovoOrdine.setIndirizzo(indirizzo);
        nuovoOrdine.setDataAcquisto(LocalDate.now());
        nuovoOrdine.setStato(StatoOrdine.IN_ATTESA);
        nuovoOrdine.setDataConsegnaStimata(LocalDate.now().plusDays(3));
        nuovoOrdine.setUtente(utente);

        // Salva l'ordine per ottenere un ID generato dal database.
        // L'oggetto 'ordineSalvato' è ora un'entità "managed" da JPA e ha un ID valido.
        Ordine ordineSalvato = ordineRepository.save(nuovoOrdine);

        // --- 3. LOGICA DI BUSINESS E CREAZIONE DEI DETTAGLI "FIGLI" ---
        List<DettagliOrdine> dettagliOrdine = carrello.stream()
                .map(cartElement -> {
                    Prodotto prodotto = cartElement.getProdotto();
                    stockService.reserveStock(filialeOnlineName, prodotto.getId(), cartElement.getQuantita());

                    DettagliOrdine dettaglio = new DettagliOrdine();

                    // Usa l'ID dell'ordine appena salvato
                    OrdineProdottoId dettagliId = new OrdineProdottoId(ordineSalvato.getId(), prodotto.getId());
                    dettaglio.setId(dettagliId);

                    // Imposta le relazioni su entrambi i lati per coerenza
                    dettaglio.setOrdine(ordineSalvato);
                    dettaglio.setProdotto(prodotto);
                    dettaglio.setQuantita(cartElement.getQuantita());

                    return dettaglio;
                })
                .collect(Collectors.toCollection(ArrayList::new));

        // Salva la lista di tutti i dettagli ordine creati
        dettagliOrdineService.createByList(dettagliOrdine);

        // --- 4. AGGIORNAMENTO FINALE E AZIONI POST-PERSISTENZA ---
        // Ora che abbiamo i dettagli, calcoliamo il totale e aggiorniamo l'ordine

        //Logica per la spedizione gratuita
        double totaleFinale = carrelloService.calcolaTotaleFinale(utenteId);
        ResponseStaticDataDTO limiteGratuita = datiStaticiService.getStaticDataByName("COSTO_MINIMO_SPEDIZIONE_GRATUITA");
        if (totaleFinale <= limiteGratuita.valore()) {
            ResponseStaticDataDTO spedizioneDati = datiStaticiService.getStaticDataByName("COSTO_SPEDIZIONE");
            totaleFinale += spedizioneDati.valore();
            ordineSalvato.setSpedizioneGratuita(false);
        } else {
            ordineSalvato.setSpedizioneGratuita(true);
        }

        ordineSalvato.setTotale(totaleFinale);

        ordineSalvato.setDettagli(dettagliOrdine);

        ordineRepository.save(ordineSalvato);

        // calcolo dei punti e aggiornamento del vantaggio utente
        int puntiTotaliUtente = utenteService.updatePointsAndAdvantagesForOrder(utenteId, ordineSalvato.getId());

        carrelloService.deleteAllItems(carrello);

        // --- 5. MAPPATURA DELLA RISPOSTA ---
        return orderMapper.toCheckoutOutputDTO(ordineSalvato, puntiTotaliUtente);
    }


     // METODO PER L'UTENTE:
     // Restituisce la lista di ordini per un singolo utente
    public List<ResponseOrderDTO> findOrdersByUserId(UUID utenteId) {
        // 1. Usa il nuovo metodo del repository per trovare gli ordini
        List<Ordine> ordini = ordineRepository.findByUtenteId(utenteId);

        // 2. Usa il mapper per convertire la lista di entità in una lista di DTO
        return ordini.stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
    }


     // METODO PER L'ADMIN:
     // Restituisce la lista di TUTTI gli ordini presenti nel sistema.
    public List<ResponseOrderDTO> findAllOrdersForAdmin() {
        // 1. Usa il metodo findAll() per recuperare tutti gli ordini
        List<Ordine> tuttiGliOrdini = ordineRepository.findAll();

        // 2. Mappa il risultato in DTO
        return tuttiGliOrdini.stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
    }
}
