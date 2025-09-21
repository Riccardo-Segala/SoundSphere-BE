package backend.controller;

import backend.config.JwtService;
import backend.dto.autenticazione.LoginRequestDTO;
import backend.service.AuthenticationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// Configuro il test per il controller di autenticazione
// Escludo la configurazione di sicurezza automatica non necessaria per il login
@WebMvcTest(controllers = AuthenticationController.class,
        excludeAutoConfiguration = {SecurityAutoConfiguration.class})
class AuthenticationControllerTest {
    // Inietto MockMvc per simulare le richieste HTTP
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // Mock del service che il controller usa direttamente
    @MockitoBean
    private AuthenticationService authenticationService;

    // Mock necessari per far avviare il contesto di sicurezza
    @MockitoBean
    private JwtService jwtService;
    @MockitoBean
    private UserDetailsService userDetailsService;


    @Test
    void login_conCredenzialiCorrette_restituisce200OkConToken() throws Exception {
        // 1. ARRANGE
        // Creo una richiesta di login valida
        LoginRequestDTO request = new LoginRequestDTO("utente@test.com", "password123");
        String fakeToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0In0.fakeToken";

        // Configuro il mock: quando viene chiamato login(), restituisco il token finto.
        when(authenticationService.login(any(LoginRequestDTO.class))).thenReturn(fakeToken);

        // 2. ACT & ASSERT
        // Simulo la richiesta POST al controller e verifico la risposta
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk()) // Verifico che lo stato sia 200 OK
                .andExpect(jsonPath("$.token").value(fakeToken)); // Verifico il JSON di risposta
    }


    @Test
    void login_conPasswordErrata_restituisce401Unauthorized() throws Exception {
        // 1. ARRANGE
        LoginRequestDTO request = new LoginRequestDTO("utente@test.com", "password_sbagliata");

        // Configuro il mock per fallire, lanciando l'eccezione che il controller si aspetta.
        when(authenticationService.login(any(LoginRequestDTO.class)))
                .thenThrow(new BadCredentialsException("Email o Password errati"));

        // 2. ACT & ASSERT
        // Simulo la richiesta POST e verifico la risposta
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized()) // Verifico che lo stato sia 401
                .andExpect(content().string("Email o Password errati")); // Verifico il messaggio di errore
    }


    @Test
    void login_conEmailNonTrovata_restituisce401Unauthorized() throws Exception {
        // 1. ARRANGE
        LoginRequestDTO request = new LoginRequestDTO("email@inesistente.com", "password123");

        // Configuro il mock per lanciare l'altra eccezione gestita dal controller.
        when(authenticationService.login(any(LoginRequestDTO.class)))
                .thenThrow(new UsernameNotFoundException("Email non trovata"));

        // 2. ACT & ASSERT
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized()) // Stato 401
                .andExpect(content().string("Email non trovata")); // Messaggio di errore specifico
    }
}
