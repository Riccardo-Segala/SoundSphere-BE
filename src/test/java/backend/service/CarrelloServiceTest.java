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

// Questa annotazione attiva le funzionalità di Mockito per i test
@ExtendWith(MockitoExtension.class)
class CarrelloServiceTest {

    @Mock // 1. Dice a Mockito di creare un oggetto finto di CarrelloRepository
    private CarrelloRepository carrelloRepository;

    @InjectMocks // 2. Crea un'istanza REALE di CarrelloService e ci "inietta" dentro i mock definiti sopra
    private CarrelloService carrelloService;

    @Test
        // Identifica questo metodo come un test
    void calcolaTotaleParziale_conCarrelloPieno_restituisceSommaCorretta() {

        // 1. ARRANGE
        // a) Crea un utente e dei prodotti finti per il test
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

        // b) Ora usa il nuovo costruttore! È molto più pulito.
        Carrello riga1 = new Carrello(utenteFinto, chitarra, 1); // 1 chitarra
        Carrello riga2 = new Carrello(utenteFinto, violino, 2); // 2 violini

        List<Carrello> righeCarrelloSimulate = Arrays.asList(riga1, riga2);

        // c) Configura il mock come prima
        when(carrelloRepository.findCartByUserId(utenteId)).thenReturn(righeCarrelloSimulate);

        // 2. ACT (Azione)
        // Chiamiamo il metodo che vogliamo testare
        double totaleCalcolato = carrelloService.calcolaTotaleParziale(utenteId);


        // 3. ASSERT (Verifica)
        // Controlliamo che il risultato sia quello che ci aspettiamo
        // Calcolo atteso: (300.0 * 1) + (500.0 * 2) = 300.0 + 1000.0 = 1300.0
        assertEquals(1300.0, totaleCalcolato, "Il totale del carrello non è stato calcolato correttamente.");
    }

    // ... dentro la classe CarrelloServiceTest

    @Test
    void calcolaTotaleParziale_conCarrelloVuoto_restituisceZero() {

        // 1. ARRANGE
        UUID utenteId = UUID.randomUUID();

        // Istruiamo il mock a restituire una lista vuota per questo utente
        when(carrelloRepository.findCartByUserId(utenteId)).thenReturn(Collections.emptyList());

        // 2. ACT
        double totaleCalcolato = carrelloService.calcolaTotaleParziale(utenteId);

        // 3. ASSERT
        // Verifichiamo che il risultato sia esattamente 0
        assertEquals(0.0, totaleCalcolato, "Il totale per un carrello vuoto dovrebbe essere 0.");
    }

}
