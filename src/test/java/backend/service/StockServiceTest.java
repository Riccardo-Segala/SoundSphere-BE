package backend.service;

import backend.model.Filiale;
import backend.model.Stock;
import backend.model.embeddable.FilialeProdottoId;
import backend.repository.StockRepository;
import backend.exception.OutOfStockException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StockServiceTest {

    @Mock
    private FilialeService filialeService;

    @Mock
    private StockRepository stockRepository;

    @InjectMocks
    private StockService stockService;


    @Test
    void reserveStock_conStockDisponibile_aggiornaCorrettamenteLaQuantita() {
        // 1. ARRANGE
        // Costruisco i dati per il test
        String nomeFiliale = "Filiale Centrale";
        UUID prodottoId = UUID.randomUUID();
        int quantitaRichiesta = 10;
        int quantitaIniziale = 100;

        // Creo una filiale finta
        Filiale filialeFinta = new Filiale();
        filialeFinta.setId(UUID.randomUUID());

        //Creo uno stock finto con quantità iniziale
        Stock stockFinto = new Stock();
        stockFinto.setQuantita(quantitaIniziale);

        // Configuro i mock
        when(filialeService.getByName(nomeFiliale)).thenReturn(filialeFinta);
        when(stockRepository.findById(any(FilialeProdottoId.class))).thenReturn(Optional.of(stockFinto));

        // 2. ACT
        // Eseguo il metodo da testare
        stockService.reserveStock(nomeFiliale, prodottoId, quantitaRichiesta);

        // 3. ASSERT
        // Verifico che la quantità nell'oggetto stock sia stata decrementata correttamente
        int quantitaFinaleAttesa = quantitaIniziale - quantitaRichiesta; // 100 - 10 = 90
        assertEquals(quantitaFinaleAttesa, stockFinto.getQuantita());
    }


    @Test
    void reserveStock_conStockNonDisponibile_lanciaOutOfStockException() {
        // 1. ARRANGE
        // Configuro i dati per il test
        String nomeFiliale = "Filiale Centrale";
        UUID prodottoId = UUID.randomUUID();
        int quantitaIniziale = 5;
        int quantitaRichiesta = 10; // Chiedo più di quanto disponibile

        Filiale filialeFinta = new Filiale();
        filialeFinta.setId(UUID.randomUUID());

        Stock stockFinto = new Stock();
        stockFinto.setQuantita(quantitaIniziale);

        // Configuro i mock
        when(filialeService.getByName(nomeFiliale)).thenReturn(filialeFinta);
        when(stockRepository.findById(any(FilialeProdottoId.class))).thenReturn(Optional.of(stockFinto));

        // 2. ACT & ASSERT
        // Verifico che venga lanciata l'eccezione corretta
        assertThrows(OutOfStockException.class, () -> {
            stockService.reserveStock(nomeFiliale, prodottoId, quantitaRichiesta);
        });

        // 3. VERIFY - Controllo per sicurezza
        // Verifico che la quantità non sia stata modificata
        assertEquals(quantitaIniziale, stockFinto.getQuantita());
    }


    @Test
    void reserveStock_conStockNonEsistente_lanciaEntityNotFoundException() {
        // 1. ARRANGE
        // Configuro i dati per il test
        String nomeFiliale = "Filiale Centrale";
        UUID prodottoId = UUID.randomUUID();
        int quantitaRichiesta = 10;

        Filiale filialeFinta = new Filiale();
        filialeFinta.setId(UUID.randomUUID());

        when(filialeService.getByName(nomeFiliale)).thenReturn(filialeFinta);

        // La condizione chiave: il repository non trova nulla e restituisce un Optional vuoto
        when(stockRepository.findById(any(FilialeProdottoId.class))).thenReturn(Optional.empty());

        // 2. ACT & ASSERT
        // Verifico che venga lanciata l'eccezione corretta quando lo stock non esiste
        assertThrows(EntityNotFoundException.class, () -> {
            stockService.reserveStock(nomeFiliale, prodottoId, quantitaRichiesta);
        });
    }
}
