package backend.controller;


import backend.config.JwtService;
import backend.dto.checkout.CheckoutInputDTO;
import backend.dto.checkout.CheckoutOutputDTO;
import backend.mapper.OrderMapper;
import backend.model.*;
import backend.security.CustomUserDetails;
import backend.service.OrdineService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;


import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// 1. Configuro il contesto di test per il controller OrdineController con @WebMvcTest
@WebMvcTest(OrdineController.class)
class OrdineControllerTest {

    // 2. Inietto gli strumenti di test forniti dal contesto @WebMvcTest
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // 3. Uso @MockitoBean per creare i mock necessari e iniettarli nel contesto di test
    @MockitoBean
    private OrdineService ordineService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @MockitoBean
    private OrderMapper orderMapper;

    // =================================================================================
    // TEST DEL CASO FELICE
    // =================================================================================
    @Test
    void checkout_conUtenteAutorizzato_restituisce201Created() throws Exception {
        // ARRANGE
        // Preparo i dati di input e output finti
        UUID utenteId = UUID.randomUUID();
        UUID indirizzoId = UUID.randomUUID();
        UUID metodoPagamentoId = UUID.randomUUID();
        CheckoutInputDTO inputDto = new CheckoutInputDTO(metodoPagamentoId, indirizzoId);

        // Creo il permesso e il ruolo finti di cui ho bisogno da associare all'utente finto
        Permesso permessoAcquisto = new Permesso();
        permessoAcquisto.setNome("ACQUISTO");

        Ruolo ruoloUtente = new Ruolo();
        ruoloUtente.setNome("UTENTE");
        ruoloUtente.setPermessi(Set.of(permessoAcquisto));

        Utente utenteFinto = new Utente();
        utenteFinto.setId(utenteId);
        utenteFinto.setEmail("test@example.com");
        utenteFinto.setPassword("password_finta");

        // Associo il ruolo (con il permesso) all'utente finto
        UtenteRuolo utenteRuoloFinto = new UtenteRuolo(utenteFinto, ruoloUtente, null);
        utenteFinto.setUtenteRuoli(Set.of(utenteRuoloFinto));
        // Creo un CustomUserDetails basato sull'utente finto con i permessi corretti
        CustomUserDetails userDetails = new CustomUserDetails(utenteFinto);

        CheckoutOutputDTO outputDto = new CheckoutOutputDTO(
                UUID.randomUUID(), "ORD-123", LocalDate.now(), "IN_ATTESA",
                BigDecimal.valueOf(200.0), null, LocalDate.now().plusDays(3),
                Collections.emptyList(), 100);

        // Configuro il mock del servizio per restituire il DTO di output finto
        when(ordineService.checkout(any(CheckoutInputDTO.class), any(UUID.class))).thenReturn(outputDto);

        // ACT & ASSERT
        // Eseguo la chiamata HTTP simulata con MockMvc, includendo l'utente autenticato
        mockMvc.perform(post("/api/ordini/checkout")
                        .with(user(userDetails))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.numeroOrdine").value("ORD-123"));
    }

    // =================================================================================
    // TEST DEL CASO NON FELICE
    // =================================================================================
    @Test
    @WithMockUser // Simula un utente loggato SENZA il permesso 'ACQUISTO'
    void checkout_conUtenteNonAutorizzato_restituisce403Forbidden() throws Exception {
        // ARRANGE
        // Preparo i dati di input finti
        UUID indirizzoId = UUID.randomUUID();
        UUID metodoPagamentoId = UUID.randomUUID();
        CheckoutInputDTO inputDto = new CheckoutInputDTO(metodoPagamentoId, indirizzoId);

        // ACT & ASSERT
        // Eseguo la chiamata HTTP simulata con MockMvc
        mockMvc.perform(post("/api/ordini/checkout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isForbidden());

        // VERIFY
        // Verifico che checkout non sia stato chiamato
        verify(ordineService, never()).checkout(any(), any());
    }
}