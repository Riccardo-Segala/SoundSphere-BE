package backend.controller;

import backend.dto.autenticazione.JwtResponseDTO;
import backend.dto.autenticazione.LoginRequestDTO;
import backend.dto.utente.CreateUserDTO;
import backend.service.AutenticazioneService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AutenticazioneController {

    private final AutenticazioneService service;

    @PostMapping("/register")
    public ResponseEntity<JwtResponseDTO> register(@RequestBody CreateUserDTO request) {
        return ResponseEntity.ok(new JwtResponseDTO(service.register(request)));
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponseDTO> login(@RequestBody LoginRequestDTO request) {
        return ResponseEntity.ok(new JwtResponseDTO(service.login(request)));
    }
}
