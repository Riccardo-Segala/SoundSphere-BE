package backend.service;

import backend.config.JwtService;
import backend.dto.autenticazione.JwtResponseDTO;
import backend.dto.autenticazione.LoginRequestDTO;
import backend.dto.dipendente.CreateEmployeeDTO;
import backend.dto.utente.CreateUserDTO;
import backend.mapper.EmployeeMapper;
import backend.mapper.UserMapper;
import backend.model.*;
import backend.model.enums.Tipologia;
import backend.repository.FilialeRepository;
import backend.repository.RuoloRepository;
import backend.repository.UtenteRepository;
import backend.repository.VantaggioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UtenteRepository repository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final VantaggioRepository vantaggioRepository;
    private final EmployeeMapper employeeMapper;
    private final FilialeRepository filialeRepository;
    private final RuoloRepository ruoloRepository;

    public JwtResponseDTO registerUser(CreateUserDTO dto) {
        if (repository.existsByEmail(dto.email())) {
            throw new IllegalArgumentException("Email già in uso.");
        }
        // Trova il ruolo "UTENTE"
        Ruolo ruoloUtente = ruoloRepository.findByNome("UTENTE")
                .orElseThrow(() -> new IllegalStateException("Ruolo 'UTENTE' non trovato"));
        // Creo Utente con MapStruct
        Utente utente = userMapper.fromCreateDto(dto);
        utente.setPassword(passwordEncoder.encode(dto.password()));
        utente.setTipologia(Tipologia.UTENTE);
        utente.setPunti(0);
        utente.setDataRegistrazione(java.time.LocalDate.now());

        // Assegno vantaggio di default
        UUID defaultVantaggioId = UUID.fromString("f8c3b3e0-4f5a-4a3c-9c7b-3f1a2b3c4d5e");
        Vantaggio vantaggioDefault = vantaggioRepository.findById(defaultVantaggioId)
                .orElseThrow(() -> new IllegalStateException("Vantaggio default non trovato"));
        utente.setVantaggio(vantaggioDefault);

        utente.setRuoli(Collections.singleton(ruoloUtente));
        repository.save(utente);
        // Genera il token per l'utente appena registrato
        String token = jwtService.generateToken(utente);
        long expiresIn = 3600L; // Esempio di durata del token

        // Costruisci e restituisci il DTO completo
        return new JwtResponseDTO(
                token,
                expiresIn,
                utente.getId(),
                utente.getNome(),
                utente.getCognome(),
                utente.getEmail(),
                utente.getRuoli().stream().map(Ruolo::getNome).collect(Collectors.toList())
        );
    }

    public JwtResponseDTO registerEmployee(CreateEmployeeDTO request) {
        CreateUserDTO userDto = request.utente();

        if (repository.existsByEmail(userDto.email())) {
            throw new IllegalArgumentException("Email già in uso.");
        }

        Ruolo ruoloDipendente = ruoloRepository.findByNome("DIPENDENTE")
                .orElseThrow(() -> new IllegalStateException("Ruolo DIPENDENTE non trovato"));

        // Creo il Dipendente dai campi extra (MapStruct)
        Dipendente dipendente = employeeMapper.fromCreateDto(request);

        // Mappo i campi comuni da userDto → Dipendente
        userMapper.partialUpdateFromCreate(userDto, dipendente);

        // Imposto la data di registrazione
        dipendente.setDataRegistrazione(java.time.LocalDate.now());

        // Campi aggiuntivi specifici
        dipendente.setPassword(passwordEncoder.encode(userDto.password()));
        dipendente.setTipologia(Tipologia.DIPENDENTE);

        // Setto il vantaggio di default
        UUID defaultVantaggioId = UUID.fromString("f8c3b3e0-4f5a-4a3c-9c7b-3f1a2b3c4d5e");
        Vantaggio vantaggioDefault = vantaggioRepository.findById(defaultVantaggioId)
                .orElseThrow(() -> new IllegalStateException("Vantaggio default non trovato"));
        dipendente.setVantaggio(vantaggioDefault);

        // Associo la filiale (controllo esistenza)
        Filiale filiale = filialeRepository.findById(request.filialeId())
                .orElseThrow(() -> new IllegalStateException("Filiale non trovata"));
        dipendente.setFiliale(filiale);

        dipendente.setRuoli(Collections.singleton(ruoloDipendente));

        repository.save(dipendente);
        // Genera il token
        String token = jwtService.generateToken(dipendente);
        long expiresIn = 3600L; // Esempio di durata del token

        // Costruisci e restituisci il DTO completo con i dati del dipendente
        return new JwtResponseDTO(
                token,
                expiresIn,
                dipendente.getId(),
                dipendente.getNome(),
                dipendente.getCognome(),
                dipendente.getEmail(),
                dipendente.getRuoli().stream().map(Ruolo::getNome).collect(Collectors.toList())
        );
    }



    public JwtResponseDTO login(LoginRequestDTO request) {
        // 1. Cerca l'utente con ruoli e permessi
        var user = repository.findByEmail(request.email())
                .orElseThrow(() -> new UsernameNotFoundException("Email non registrata."));

        // 2. Autentica username e password
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.email(),
                            request.password()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Password errata.");
        }

        // 3. Converte i permessi in GrantedAuthority
        List<SimpleGrantedAuthority> authorities = user.getRuoli().stream()
                .flatMap(ruolo -> ruolo.getPermessi().stream())
                .map(permesso -> new SimpleGrantedAuthority(permesso.getNome()))
                .distinct()
                .collect(Collectors.toList());

        // 4. Crea un UserDetails con le authorities
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                authorities
        );

        // 5. Genera il JWT includendo le authorities
        String token = jwtService.generateToken(userDetails);
        // Calcolo della durata del token (es. 1 ora)
        long expiresIn = 3600L;

        // Costruisco e restituisco il DTO completo, inclusi i dati dell'utente
        return new JwtResponseDTO(
                token,
                expiresIn,
                user.getId(), // Popolo l'ID dell'utente
                user.getNome(),
                user.getCognome(),
                user.getEmail(),
                user.getRuoli().stream().map(ruolo -> ruolo.getNome()).collect(Collectors.toList())
        );
    }

}