package backend.controller;

import backend.dto.autenticazione.JwtResponseDTO;
import backend.dto.autenticazione.LoginRequestDTO;
import backend.dto.dipendente.CreateEmployeeDTO;
import backend.dto.utente.CreateUserDTO;
import backend.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path="/api/auth", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/register/user")
    public ResponseEntity<JwtResponseDTO> registerUser(@RequestBody CreateUserDTO request) {
        return ResponseEntity.ok(new JwtResponseDTO(service.registerUser(request)));
    }

    @PostMapping("/register/employee")
    public ResponseEntity<JwtResponseDTO> registerEmployee(@RequestBody CreateEmployeeDTO request) {
        return ResponseEntity.ok(new JwtResponseDTO(service.registerEmployee(request)));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO request) {
        try {
            // La logica di login nel service lancerà eccezioni specifiche
            String token = service.login(request);

            // In caso di successo, restituisco il token
            return ResponseEntity.ok(new JwtResponseDTO(token));

        } catch (UsernameNotFoundException e) {
            // Gestione errore: email non trovata
            // Restituisci una risposta 401 Unauthorized con un messaggio di errore specifico
            return new ResponseEntity<>("Email non trovata", HttpStatus.UNAUTHORIZED);

        } catch (BadCredentialsException e) {
            // Restituisci una risposta 401 Unauthorized con un messaggio di errore specifico
            return new ResponseEntity<>("Email o Password errati", HttpStatus.UNAUTHORIZED);

        }
    }
}
