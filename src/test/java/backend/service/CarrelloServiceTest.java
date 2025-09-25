package backend.service;

import backend.repository.CarrelloRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import backend.model.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class CarrelloServiceTest {

    @Mock
    private CarrelloRepository carrelloRepository;

    @InjectMocks
    private CarrelloService carrelloService;

    @Test
    void calcolaTotaleParziale_conCarrelloPieno_restituisceSommaCorretta() {

        // 1. ARRANGE
        // Creo un utente e dei prodotti finti per il test
        UUID utenteId = UUID.randomUUID();
        Utente utenteFinto = new Utente();
        utenteFinto.setId(utenteId);

        UUID prodottoId1 = UUID.randomUUID();
        Prodotto chitarra = new Prodotto();
        chitarra.setId(prodottoId1);
        chitarra.setPrezzo(300.0);

        UUID prodottoId2 = UUID.randomUUID();
        Prodotto violino = new Prodotto();
        violino.setId(prodottoId2);
        violino.setPrezzo(500.0);

        Carrello riga1 = new Carrello(utenteFinto, chitarra, 1); // 1 chitarra
        Carrello riga2 = new Carrello(utenteFinto, violino, 2); // 2 violini

        List<Carrello> righeCarrelloSimulate = Arrays.asList(riga1, riga2);

        // Configuro il mock
        when(carrelloRepository.findCartByUserId(utenteId)).thenReturn(righeCarrelloSimulate);

        // 2. ACT (Azione)
        // Chiamo il metodo che voglo testare
        double totaleCalcolato = carrelloService.calcolaTotaleParziale(utenteId);


        // 3. ASSERT (Verifica)
        // Controllo che il risultato sia quello che mi aspetto
        // Calcolo atteso: (300.0 * 1) + (500.0 * 2) = 300.0 + 1000.0 = 1300.0
        assertEquals(1300.0, totaleCalcolato, "Il totale del carrello non è stato calcolato correttamente.");
    }


    @Test
    void calcolaTotaleParziale_conCarrelloVuoto_restituisceZero() {

        // 1. ARRANGE
        UUID utenteId = UUID.randomUUID();

        // Istruisco il mock a restituire una lista vuota per questo utente
        when(carrelloRepository.findCartByUserId(utenteId)).thenReturn(Collections.emptyList());

        // 2. ACT
        double totaleCalcolato = carrelloService.calcolaTotaleParziale(utenteId);

        // 3. ASSERT
        // Verifico che il risultato sia esattamente 0
        assertEquals(0.0, totaleCalcolato, "Il totale per un carrello vuoto dovrebbe essere 0.");
    }

}
