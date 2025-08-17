package backend.controller;

import backend.dto.autenticazione.JwtResponseDTO;
import backend.dto.autenticazione.LoginRequestDTO;
import backend.dto.dipendente.CreateEmployeeDTO;
import backend.dto.utente.CreateUserDTO;
import backend.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/register/user")
    public ResponseEntity<JwtResponseDTO> registerUser(@RequestBody CreateUserDTO request) {
        // La logica di login nel service lancerà eccezioni specifiche
        JwtResponseDTO response = service.registerUser(request);

        // In caso di successo, restituisco il token
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register/employee")
    public ResponseEntity<JwtResponseDTO> registerEmployee(@RequestBody CreateEmployeeDTO request) {
        // La logica di login nel service lancerà eccezioni specifiche
        JwtResponseDTO response = service.registerEmployee(request);

        // In caso di successo, restituisco il token
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO request) {
        try {
            // La logica di login nel service lancerà eccezioni specifiche
            JwtResponseDTO response = service.login(request);

            // In caso di successo, restituisco il token
            return ResponseEntity.ok(response);

        } catch (UsernameNotFoundException e) {
            // Gestione errore: email non trovata
            // Restituisci una risposta 401 Unauthorized con un messaggio di errore specifico
            return new ResponseEntity<>("Email non trovata", HttpStatus.UNAUTHORIZED);

        } catch (BadCredentialsException e) {
            // Restituisci una risposta 401 Unauthorized con un messaggio di errore specifico
            return new ResponseEntity<>("Password errata", HttpStatus.UNAUTHORIZED);

        }
    }
}
