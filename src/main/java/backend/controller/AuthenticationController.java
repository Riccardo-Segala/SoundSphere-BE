package backend.controller;

import backend.dto.autenticazione.JwtResponseDTO;
import backend.dto.autenticazione.LoginRequestDTO;
import backend.dto.dipendente.CreateEmployeeDTO;
import backend.dto.utente.CreateUserDTO;
import backend.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
        return ResponseEntity.ok(new JwtResponseDTO(service.registerUser(request)));
    }

    @PostMapping("/register/employee")
    public ResponseEntity<JwtResponseDTO> registerEmployee(@RequestBody CreateEmployeeDTO request) {
        return ResponseEntity.ok(new JwtResponseDTO(service.registerEmployee(request)));
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponseDTO> login(@RequestBody LoginRequestDTO request) {
        return ResponseEntity.ok(new JwtResponseDTO(service.login(request)));
    }
}
