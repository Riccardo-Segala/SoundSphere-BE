package backend.controller;


import backend.config.JwtService;
import backend.controller.admin.AdminUtenteController;
import backend.dto.utente.ResponseUserDTO;
import backend.dto.utente.admin.CreateUserFromAdminDTO;
import backend.mapper.OrderMapper;
import backend.service.UtenteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminUtenteController.class) // Specifica il controller da testare
class AdminUtenteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // Mock del service che il controller usa direttamente
    @MockitoBean
    private UtenteService utenteService;

    // Mock necessari per far avviare il contesto di sicurezza
    @MockitoBean
    private JwtService jwtService;
    @MockitoBean
    private UserDetailsService userDetailsService;
    @MockitoBean
    private OrderMapper orderMapper; // Se ancora richiesto dal contesto


    @Test
    @WithMockUser(roles = "ADMIN")
    void createUser_conDatiValidi_restituisce200OkConUtente() throws Exception {
        // 1. ARRANGE
        // Creo i DTO con dati di esempio per la richiesta e la risposta
        CreateUserFromAdminDTO requestDto = new CreateUserFromAdminDTO(
                "Mario", "Rossi", "mario.rossi@test.com", "password123",
                null, null, null, null, null
        );

        ResponseUserDTO responseDto = new ResponseUserDTO(
                UUID.randomUUID(), "Mario", "Rossi", "mario.rossi@test.com",
                null, null, null, null, null, 0, null, null
        );

        // Configuro il mock del service: quando viene chiamato, restituisce il DTO di risposta
        when(utenteService.createUser(any(CreateUserFromAdminDTO.class))).thenReturn(responseDto);

        // 2. ACT & ASSERT
        // Eseguo la richiesta POST e verifico la risposta
        mockMvc.perform(post("/api/admin/utenti")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk()) // Verifico che lo stato sia 200 OK
                .andExpect(jsonPath("$.email").value("mario.rossi@test.com"))
                .andExpect(jsonPath("$.nome").value("Mario"));
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    void createUser_conEmailDuplicata_restituisce400BadRequest() throws Exception {
        // 1. ARRANGE
        // Creo il DTO di richiesta con un'email che simulerò come già esistente
        CreateUserFromAdminDTO requestDto = new CreateUserFromAdminDTO(
                "Mario", "Rossi", "mario.rossi@test.com", "password123",
                null, null, null, null, null
        );

        // Configuro il mock per lanciare l'eccezione quando viene chiamato
        when(utenteService.createUser(any(CreateUserFromAdminDTO.class)))
                .thenThrow(new IllegalArgumentException("Email già in uso."));

        // 2. ACT & ASSERT
        // Eseguo la richiesta POST e verifico che venga restituito 400 Bad Request
        mockMvc.perform(post("/api/admin/utenti")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest()) // Verifichiamo lo stato 400 Bad Request
                .andExpect(jsonPath("$.message").value("Email già in uso.")); // Verifico il messaggio di errore
    }
}
