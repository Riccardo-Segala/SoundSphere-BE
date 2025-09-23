package backend.controller;

import backend.config.JwtService;
import backend.exception.RestExceptionHandler;
import backend.mapper.PaymentMethodMapper;
import backend.model.Utente;
import backend.security.CustomUserDetails;
import backend.service.MetodoPagamentoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import jakarta.persistence.EntityNotFoundException;
import java.util.UUID;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MetodoPagamentoController.class)
@Import(RestExceptionHandler.class) // Importo il gestore per testare gli errori
class MetodoPagamentoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // Mock del service usato dal controller
    @MockitoBean
    private MetodoPagamentoService metodoPagamentoService;

    @MockitoBean
    private PaymentMethodMapper paymentMethodMapper;

    // Mock necessari per il contesto di sicurezza
    @MockitoBean
    private JwtService jwtService;
    @MockitoBean
    private UserDetailsService userDetailsService;

    @Test
    void deletePaymentMethod_conUtenteAutorizzato_restituisce204NoContent() throws Exception {
        // 1. ARRANGE
        // Creo ID finti per utente e metodo
        UUID userId = UUID.randomUUID();
        UUID methodId = UUID.randomUUID();

        // Creo l'utente finto per simulare l'@AuthenticationPrincipal
        Utente utenteFinto = new Utente();
        utenteFinto.setId(userId);
        CustomUserDetails userDetails = new CustomUserDetails(utenteFinto);

        // Configuro il mock del service: quando 'delete' viene chiamato, non fare nulla (successo)
        doNothing().when(metodoPagamentoService).delete(userId, methodId);

        // 2. ACT & ASSERT
        // Eseguo la richiesta DELETE simulando l'utente autenticato
        mockMvc.perform(delete("/api/metodi-pagamento/{id}", methodId)
                        .with(user(userDetails)) // Simula l'utente autenticato
                        .with(csrf())) // Necessario per le richieste DELETE
                .andExpect(status().isNoContent()); // Verifico lo stato 204
    }

    @Test
    void deletePaymentMethod_conMetodoInesistente_restituisce404NotFound() throws Exception {
        // 1. ARRANGE
        // Creo ID finti per utente e metodo
        UUID userId = UUID.randomUUID();
        UUID methodId = UUID.randomUUID(); // Un ID che non esiste o non appartiene all'utente

        // Creo l'utente finto per simulare l'@AuthenticationPrincipal
        Utente utenteFinto = new Utente();
        utenteFinto.setId(userId);
        CustomUserDetails userDetails = new CustomUserDetails(utenteFinto);

        // Configuro il mock per lanciare l'eccezione quando 'delete' viene chiamato
        doThrow(new EntityNotFoundException("Metodo non trovato"))
                .when(metodoPagamentoService).delete(userId, methodId);

        // 2. ACT & ASSERT
        // Eseguo la richiesta DELETE simulando l'utente autenticato
        mockMvc.perform(delete("/api/metodi-pagamento/{id}", methodId)
                        .with(user(userDetails))
                        .with(csrf()))
                .andExpect(status().isNotFound()); // Verifico lo stato 404
    }
}
