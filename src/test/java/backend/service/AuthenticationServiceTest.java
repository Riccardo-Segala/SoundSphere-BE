package backend.service;

import backend.config.JwtService;
import backend.dto.autenticazione.LoginRequestDTO;
import backend.model.Utente;
import backend.repository.UtenteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UtenteRepository utenteRepository;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthenticationService authenticationService;


    @Test
    void login_conCredenzialiCorrette_restituisceToken() {
        // 1. ARRANGE
        // Creo un LoginRequestDTO di esempio e un Utente finto
        LoginRequestDTO request = new LoginRequestDTO("utente@test.com", "password123");
        Utente utenteFinto = new Utente();
        utenteFinto.setEmail(request.email());

        // Configuro i mock per lo scenario di successo:
        // a) L'AuthenticationManager non lancia eccezioni quando chiamato
        // b) Il repository trova l'utente
        when(utenteRepository.findByEmail(request.email())).thenReturn(Optional.of(utenteFinto));
        // c) Il JwtService genera un token finto
        when(jwtService.generateToken(utenteFinto)).thenReturn("fake.jwt.token");


        // 2. ACT
        // Chiamo il metodo che voglio testare
        String token = authenticationService.login(request);


        // 3. ASSERT & VERIFY
        // Verifico che il token restituito sia quello che ci aspettiamo
        assertNotNull(token);
        assertEquals("fake.jwt.token", token);

        // Verifico che i metodi dei mock siano stati chiamati correttamente
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService).generateToken(utenteFinto);
    }


    @Test
    void login_conCredenzialiErrate_lanciaBadCredentialsException() {
        // 1. ARRANGE
        LoginRequestDTO request = new LoginRequestDTO("utente@test.com", "password_sbagliata");

        // Configuro l'AuthenticationManager per fallire!
        // Lancia un'eccezione quando viene chiamato
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Credenziali non valide"));


        // 2. ACT & ASSERT
        // Verifico che la chiamata a login() lanci esattamente l'eccezione che mi aspetto
        assertThrows(BadCredentialsException.class, () -> {
            authenticationService.login(request);
        });


        // 3. VERIFY
        // Verifico che il flusso si sia interrotto e che il token NON sia mai stato generato.
        verify(utenteRepository, never()).findByEmail(anyString());
        verify(jwtService, never()).generateToken(any());
    }


    @Test
    void login_utenteAutenticatoMaNonTrovato_lanciaUsernameNotFoundException() {
        // 1. ARRANGE
        LoginRequestDTO request = new LoginRequestDTO("utente@test.com", "password123");

        // L'autenticazione ha successo ma il repository non trova l'utente!
        when(utenteRepository.findByEmail(request.email())).thenReturn(Optional.empty());

        // 2. ACT & ASSERT
        // Verifico che la chiamata a login() lanci esattamente l'eccezione che mi aspetto
        assertThrows(UsernameNotFoundException.class, () -> {
            authenticationService.login(request);
        });

        // 3. VERIFY
        verify(jwtService, never()).generateToken(any());
    }
}
