package backend.service;

import backend.dto.checkout.CheckoutInputDTO;
import backend.dto.checkout.CheckoutOutputDTO;
import backend.dto.checkout.ProductOrderInputDTO;
import backend.mapper.OrderMapper;
import backend.model.*;
import backend.model.embeddable.OrdineProdottoId;
import backend.model.enums.StatoOrdine;
import backend.repository.OrdineRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OrdineService extends GenericService<Ordine, UUID> {
    private final OrdineRepository ordineRepository;
    private final OrderMapper orderMapper;

    @Autowired
    private UtenteService utenteService;
    @Autowired
    private IndirizzoUtenteService indirizzoUtenteService;
    @Autowired
    private MetodoPagamentoService metodoPagamentoService;
    @Autowired
    private ProdottoService prodottoService;
    @Autowired
    private StockService stockService;
    @Autowired
    private CarrelloService carrelloService;
    @Autowired
    private DettagliOrdineService dettagliOrdineService;



    public OrdineService(OrdineRepository repository, OrdineRepository ordineRepository, OrderMapper orderMapper) {
        super(repository); // Passa il repository al costruttore della classe base
        this.ordineRepository = ordineRepository;
        this.orderMapper = orderMapper;
    }

    @Transactional
    public CheckoutOutputDTO checkout(CheckoutInputDTO checkoutDto, UUID utenteId) {

        // --- 1. RECUPERO E VALIDAZIONE TRAMITE SERVICE ---
        // Le logiche complesse (es. controllo di proprietà) sono incapsulate nei rispettivi service.
        Utente utente = utenteService.findById(utenteId)
                .orElseThrow(() -> new EntityNotFoundException("Utente non valido"));
        IndirizzoUtente indirizzo = indirizzoUtenteService.findByIdAndValidateOwnership(checkoutDto.indirizzoSpedizioneId(), utenteId);
        MetodoPagamento metodoPagamento = metodoPagamentoService.findById(checkoutDto.metodoPagamentoId())
                .orElseThrow(() -> new EntityNotFoundException("Metodo di pagamento non valido"));

        // --- CORREZIONE LOGICA: Crea l'oggetto Ordine prima del loop ---
        Ordine nuovoOrdine = new Ordine();

        // --- 2. LOGICA DI BUSINESS E CALCOLI ---
        List<DettagliOrdine> dettagliOrdine = new ArrayList<>();
        BigDecimal importoTotale = BigDecimal.ZERO;

        for (ProductOrderInputDTO prodottoDto : checkoutDto.prodotti()) {
            // Delega la ricerca del prodotto
            Prodotto prodotto = prodottoService.findById(prodottoDto.prodottoId())
                    .orElseThrow(() -> new EntityNotFoundException("Prodotto non valido"));

            // --- CHIAMATA AD ALTO LIVELLO ---
            // Delega tutta la logica di controllo e aggiornamento dello stock allo StockService.
            // OrdineService non sa più come funziona lo stock internamente.
            stockService.reserveStock("online", prodotto.getId(), prodottoDto.quantita());

            // Crea la riga d'ordine (DettaglioOrdine)
            DettagliOrdine dettaglio = new DettagliOrdine();
            OrdineProdottoId dettagliId = new OrdineProdottoId(nuovoOrdine.getId(), prodotto.getId());
            dettaglio.setId(dettagliId);
            dettaglio.setOrdine(nuovoOrdine); // Associa l'ordine creato in precedenza
            dettaglio.setProdotto(prodotto);
            dettaglio.setUtente(utente);
            dettaglio.setQuantita(prodottoDto.quantita());
            dettagliOrdine.add(dettaglio);

        }

        //Calcolo Totale Finale dell'orine
        double totaleFinale = carrelloService.calcolaTotaleFinale(utenteId);

        // --- 3. CREAZIONE E PERSISTENZA DELL'ORDINE ---
        // Popola l'ordine con tutti i dati raccolti
        nuovoOrdine.setIndirizzo(indirizzo);
        nuovoOrdine.setDataAcquisto(LocalDate.now());
        nuovoOrdine.setStato(StatoOrdine.IN_ELABORAZIONE);
        nuovoOrdine.setTotale(totaleFinale);
        // Salvo nuovo ordine
        Ordine ordineSalvato = ordineRepository.save(nuovoOrdine);
        dettagliOrdineService.createList(dettagliOrdine);

        // --- 4. AZIONI POST-PERSISTENZA ---
        carrelloService.deleteAllForUser(utenteId);
        // emailService.inviaConfermaOrdine(ordineSalvato); // Esempio invio email

        // --- 5. MAPPATURA DELLA RISPOSTA ---
        return orderMapper.toCheckoutOutputDTO(ordineSalvato);
    }
}
