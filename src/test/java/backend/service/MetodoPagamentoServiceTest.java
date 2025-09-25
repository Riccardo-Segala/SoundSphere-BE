package backend.service;

import backend.model.MetodoPagamento;
import backend.repository.MetodoPagamentoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MetodoPagamentoServiceTest {

    @Mock
    private MetodoPagamentoRepository metodoPagamentoRepository;

    @InjectMocks
    private MetodoPagamentoService metodoPagamentoService;

    @Test
    void delete_conMetodoNonPrincipale_cancellaCorrettamente() {
        // 1. ARRANGE
        // Creo un metodo di pagamento che non è principale di un utente
        UUID userId = UUID.randomUUID();
        UUID methodId = UUID.randomUUID();

        MetodoPagamento metodoDaCancellare = new MetodoPagamento();
        metodoDaCancellare.setId(methodId);
        metodoDaCancellare.setMain(false); // Non è il metodo main

        // Configuro il mock per trovare il metodo
        when(metodoPagamentoRepository.findByIdAndUtenteId(methodId, userId))
                .thenReturn(Optional.of(metodoDaCancellare));

        // 2. ACT
        // Chiamo il metodo da testare
        metodoPagamentoService.delete(userId, methodId);

        // 3. VERIFY
        // Verifico che il metodo delete del repository sia stato chiamato
        // sull'oggetto corretto
        verify(metodoPagamentoRepository).delete(metodoDaCancellare);
        // Verifico che NON sia stata fatta nessuna ricerca di altri metodi
        verify(metodoPagamentoRepository, never()).findByUtente_Id(any());
    }

    @Test
    void delete_conMetodoPrincipaleEAltriMetodiEsistenti_promuoveNuovoPrincipale() {
        // 1. ARRANGE
        // Creo un metodo di pagamento principale di un utente che ha anche altri metodi
        UUID userId = UUID.randomUUID();
        UUID oldMainId = UUID.randomUUID();
        UUID newMainId = UUID.randomUUID();

        MetodoPagamento vecchioMain = new MetodoPagamento();
        vecchioMain.setId(oldMainId);
        vecchioMain.setMain(true); // È il metodo main

        MetodoPagamento futuroMain = new MetodoPagamento();
        futuroMain.setId(newMainId);
        futuroMain.setMain(false);

        // Lista di tutti i metodi dell'utente (incluso quello da cancellare)
        List<MetodoPagamento> tuttiIMetodi = List.of(vecchioMain, futuroMain);

        // Configuro il mock per trovare il metodo da cancellare
        when(metodoPagamentoRepository.findByIdAndUtenteId(oldMainId, userId))
                .thenReturn(Optional.of(vecchioMain));
        // Configuro il mock per trovare tutti i metodi dell'utente
        when(metodoPagamentoRepository.findByUtente_Id(userId)).thenReturn(tuttiIMetodi);

        // 2. ACT
        // Chiamo il metodo da testare
        metodoPagamentoService.delete(userId, oldMainId);

        // 3. ASSERT & VERIFY
        // Verifico che il nuovo metodo sia stato impostato come 'main'
        assertTrue(futuroMain.isMain());
        // Verifico che la cancellazione del vecchio metodo sia comunque avvenuta
        verify(metodoPagamentoRepository).delete(vecchioMain);
    }

    @Test
    void delete_conUltimoMetodoRimanente_cancellaSenzaErrori() {
        // 1. ARRANGE
        // Creo un metodo di pagamento principale di un utente che NON ha altri metodi
        UUID userId = UUID.randomUUID();
        UUID methodId = UUID.randomUUID();

        MetodoPagamento ultimoMetodo = new MetodoPagamento();
        ultimoMetodo.setId(methodId);
        ultimoMetodo.setMain(true); // È l'unico metodo, quindi è anche il main


        // Configuro il mock per trovare il metodo da cancellare
        when(metodoPagamentoRepository.findByIdAndUtenteId(methodId, userId))
                .thenReturn(Optional.of(ultimoMetodo));
        // Quando cerca altri metodi, restituisce una lista che contiene solo l'unico metodo
        when(metodoPagamentoRepository.findByUtente_Id(userId)).thenReturn(List.of(ultimoMetodo));

        // 2. ACT
        // Chiamo il metodo da testare
        metodoPagamentoService.delete(userId, methodId);

        // 3. VERIFY
        // L'unica cosa da verificare è che la cancellazione sia avvenuta
        verify(metodoPagamentoRepository).delete(ultimoMetodo);
    }

    @Test
    void delete_conMetodoNonAppartenenteAllUtente_lanciaEntityNotFoundException() {
        // 1. ARRANGE
        // Creo due UUID casuali per utente e metodo
        UUID userId = UUID.randomUUID();
        UUID methodId = UUID.randomUUID();

        // La condizione chiave: il repository non trova nulla e restituisce un Optional vuoto
        when(metodoPagamentoRepository.findByIdAndUtenteId(methodId, userId))
                .thenReturn(Optional.empty());

        // 2. ACT & ASSERT
        // Chiamo il metodo da testare e verifico che lanci l'eccezione attesa
        assertThrows(EntityNotFoundException.class, () -> {
            metodoPagamentoService.delete(userId, methodId);
        });

        // 3. VERIFY
        // Verifico che l'operazione di cancellazione non sia mai stata chiamata
        verify(metodoPagamentoRepository, never()).delete(any());
    }
}
