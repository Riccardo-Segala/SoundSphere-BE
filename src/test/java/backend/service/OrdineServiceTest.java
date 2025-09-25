package backend.service;

import backend.dto.checkout.CheckoutInputDTO;
import backend.dto.checkout.CheckoutOutputDTO;
import backend.dto.dati_statici.ResponseStaticDataDTO;
import backend.model.enums.StatoOrdine;
import backend.mapper.OrderMapper;
import backend.model.*;
import backend.repository.OrdineRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrdineServiceTest {

    @Mock private UtenteService utenteService;
    @Mock private IndirizzoUtenteService indirizzoUtenteService;
    @Mock private MetodoPagamentoService metodoPagamentoService;
    @Mock private CarrelloService carrelloService;
    @Mock private StockService stockService;
    @Mock private DettagliOrdineService dettagliOrdineService;
    @Mock private DatiStaticiService datiStaticiService;
    @Mock private OrdineRepository ordineRepository;
    @Mock private OrderMapper orderMapper;

    @InjectMocks
    private OrdineService ordineService;

    @Test
    void checkout_conDatiValidi_creaEsalvaOrdineCorrettamente() {

        // =================================================================================
        // 1. ARRANGE (Preparo tutto il necessario per il test)
        // =================================================================================

        // a) Dati di input di base
        UUID utenteId = UUID.randomUUID();
        UUID indirizzoId = UUID.randomUUID();
        UUID metodoPagamentoId = UUID.randomUUID();
        CheckoutInputDTO checkoutDto = new CheckoutInputDTO(indirizzoId, metodoPagamentoId);

        // b) Creo oggetti finti che i service mockati dovranno restituire
        Utente utenteFinto = new Utente();
        utenteFinto.setId(utenteId);

        IndirizzoUtente indirizzoFinto = new IndirizzoUtente();
        MetodoPagamento metodoPagamentoFinto = new MetodoPagamento();

        Prodotto prodottoFinto1 = new Prodotto();
        prodottoFinto1.setId(UUID.randomUUID());
        prodottoFinto1.setPrezzo(100.0);

        Carrello rigaCarrello1 = new Carrello(utenteFinto, prodottoFinto1, 2); // 2 x 100.0
        List<Carrello> carrelloFinto = List.of(rigaCarrello1);

        Ordine ordineDaSalvare = new Ordine();
        ordineDaSalvare.setId(UUID.randomUUID()); // Simuliamo che save() assegni un ID

        // c) Configuro il comportamento di ogni mock
        // --- Service di recupero dati ---
        when(utenteService.findById(any(UUID.class))).thenReturn(Optional.of(utenteFinto));
        when(indirizzoUtenteService.findByIdAndValidateOwnership(any(UUID.class), any(UUID.class))).thenReturn(indirizzoFinto);
        when(metodoPagamentoService.findById(any(UUID.class))).thenReturn(Optional.of(metodoPagamentoFinto));
        when(carrelloService.getCartByUtenteId(eq(utenteId))).thenReturn(carrelloFinto);

        // --- Service di calcolo e dati statici ---
        when(carrelloService.calcolaTotaleFinale(utenteId)).thenReturn(200.0); // 100.0 * 2
        when(datiStaticiService.getStaticDataByName("COSTO_MINIMO_SPEDIZIONE_GRATUITA")).thenReturn(new ResponseStaticDataDTO(UUID.randomUUID(), "SPED_GRATUITA", 50.0));

        // --- Repository: configurazione speciale per il save() ---
        // Quando viene chiamato save(), restituisco l'oggetto stesso che gli è stato passato
        // Questo simula il comportamento di JPA che restituisce l'entità "managed"
        when(ordineRepository.save(any(Ordine.class))).thenReturn(ordineDaSalvare);

        // 1. Creo un DTO finto in una variabile
        CheckoutOutputDTO outputFinto = new CheckoutOutputDTO(
                UUID.randomUUID(),
                "ORDINE-TEST-123",
                LocalDate.now(),
                "IN_ATTESA",
                BigDecimal.valueOf(200.0),
                null,
                LocalDate.now().plusDays(3),
                Collections.emptyList(),
                100
        );
        // --- Mapper ---
        // mi aspetto che il mapper venga chiamato con l'ordine salvato
        when(orderMapper.toCheckoutOutputDTO(any(Ordine.class), anyInt())).thenReturn(outputFinto);

        // =================================================================================
        // 2. ACT (Eseguo il metodo che voglio testare)
        // =================================================================================

        CheckoutOutputDTO risultato = ordineService.checkout(checkoutDto, utenteId);

        // =================================================================================
        // 3. ASSERT / VERIFY (Verifico che tutto si sia comportato come previsto)
        // =================================================================================

        // a) Verifico le chiamate critiche ai mock (`verify`)

        // È stato riservato lo stock per il nostro prodotto?
        verify(stockService, times(1)).reserveStock(nullable(String.class), eq(prodottoFinto1.getId()), eq(2));

        // È stato svuotato il carrello alla fine?
        verify(carrelloService, times(1)).deleteAllItems(carrelloFinto);

        // b) Uso un ArgumentCaptor per "catturare" l'oggetto salvato nel repository
        // Questo permette di ispezionare l'ordine che è stato effettivamente salvato
        ArgumentCaptor<Ordine> ordineCaptor = ArgumentCaptor.forClass(Ordine.class);

        // Verifico che save() sia stato chiamato 2 volte e catturo gli oggetti
        verify(ordineRepository, times(2)).save(ordineCaptor.capture());

        List<Ordine> ordiniSalvati = ordineCaptor.getAllValues();
        Ordine primoSalvataggio = ordiniSalvati.get(0);
        Ordine secondoSalvataggio = ordiniSalvati.get(1);

        // c) Ora faccio asserzioni sugli oggetti catturati!
        assertNotNull(primoSalvataggio);
        assertEquals(utenteFinto, primoSalvataggio.getUtente());
        assertEquals(StatoOrdine.IN_ATTESA, primoSalvataggio.getStato());

        assertNotNull(secondoSalvataggio);
        assertEquals(200.0, secondoSalvataggio.getTotale());
        assertTrue(secondoSalvataggio.isSpedizioneGratuita()); // Perché 200.0 > 50.0

        // d) Infine, controllo il risultato finale del metodo
        assertNotNull(risultato);
        assertEquals(outputFinto.ordineId(), risultato.ordineId());
    }


    @Test
    void checkout_conCarrelloVuoto_lanciaEccezione() {
        // 1. ARRANGE
        UUID utenteId = UUID.randomUUID();
        CheckoutInputDTO checkoutDto = new CheckoutInputDTO(UUID.randomUUID(), UUID.randomUUID());

        // Configuro i mock che vengono chiamati PRIMA del controllo del carrello
        when(utenteService.findById(utenteId)).thenReturn(Optional.of(new Utente()));
        when(indirizzoUtenteService.findByIdAndValidateOwnership(any(UUID.class), any(UUID.class))).thenReturn(new IndirizzoUtente());
        when(metodoPagamentoService.findById(any(UUID.class))).thenReturn(Optional.of(new MetodoPagamento()));

        // La condizione chiave del test: il carrello è vuoto!
        when(carrelloService.getCartByUtenteId(utenteId)).thenReturn(Collections.emptyList());


        // 2. ACT & ASSERT (Azione e Verifica combinati)
        // Uso assertThrows per verificare che la chiamata a checkout() lanci l'eccezione che mi aspetto
        // Il test passa SE l'eccezione viene lanciata, altrimenti fallisce.
        assertThrows(IllegalStateException.class, () -> {
            ordineService.checkout(checkoutDto, utenteId);
        }, "Dovrebbe lanciare un'eccezione se il carrello è vuoto");


        // 3. VERIFY (Verifica extra)
        // Verifico che, a causa dell'eccezione, NESSUNA operazione di persistenza o modifica sia avvenuta.
        verify(ordineRepository, never()).save(any(Ordine.class));
        verify(stockService, never()).reserveStock(anyString(), any(UUID.class), anyInt());
    }


    @Test
    void checkout_quandoStockNonDisponibile_lanciaEccezione() {
        // 1. ARRANGE
        UUID utenteId = UUID.randomUUID();
        CheckoutInputDTO checkoutDto = new CheckoutInputDTO(UUID.randomUUID(), UUID.randomUUID());

        Utente utenteFinto = new Utente();
        utenteFinto.setId(utenteId);

        Prodotto prodottoFinto1 = new Prodotto();
        prodottoFinto1.setId(UUID.randomUUID());

        List<Carrello> carrelloFinto = List.of(new Carrello(utenteFinto, prodottoFinto1, 1));

        // Configuro tutti i mock che vengono chiamati PRIMA della riserva dello stock
        when(utenteService.findById(utenteId)).thenReturn(Optional.of(utenteFinto));
        when(indirizzoUtenteService.findByIdAndValidateOwnership(any(), any())).thenReturn(new IndirizzoUtente());
        when(metodoPagamentoService.findById(any())).thenReturn(Optional.of(new MetodoPagamento()));
        when(carrelloService.getCartByUtenteId(utenteId)).thenReturn(carrelloFinto);
        when(ordineRepository.save(any(Ordine.class))).thenReturn(new Ordine());

        // La condizione chiave: sabotiamo lo stockService!
        // Gli dico di lanciare un'eccezione quando si prova a riservare lo stock.
        doThrow(new RuntimeException("Stock non sufficiente per il prodotto " + prodottoFinto1.getId()))
                .when(stockService).reserveStock(anyString(), eq(prodottoFinto1.getId()), anyInt());


        // 2. ACT & ASSERT
        // Verifico che la chiamata a checkout() propaghi l'eccezione lanciata dallo stockService
        assertThrows(RuntimeException.class, () -> {
            ordineService.checkout(checkoutDto, utenteId);
        });

        // 3. VERIFY
        // Verifico che la transazione si sia "bloccata".
        // L'ordine iniziale viene creato e salvato una volta, ma le operazioni successive no.
        verify(ordineRepository, times(1)).save(any(Ordine.class));

        // Verifico che il carrello NON sia stato svuotato
        verify(carrelloService, never()).deleteAllItems(any());
    }
}
