package backend.service;

import backend.config.JwtService;
import backend.dto.autenticazione.LoginRequestDTO;
import backend.dto.utente.CreateUserDTO;
import backend.mapper.UserMapper;
import backend.model.Utente;
import backend.repository.UtenteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AutenticazioneService {

    private final UtenteRepository repository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public String register(CreateUserDTO request) {
        if (repository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email già in uso.");
        }
        Utente utente = userMapper.fromCreateDto(request);
        utente.setPassword(passwordEncoder.encode(request.password()));
        // Qui puoi impostare valori di default, es. ruoli, punti, etc.
        repository.save(utente);
        return jwtService.generateToken(utente);
    }

    public String login(LoginRequestDTO request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );
        var user = repository.findByEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));
        return jwtService.generateToken(user);
    }
}