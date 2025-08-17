package backend.service;

import backend.config.JwtService;
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

    public String registerUser(CreateUserDTO dto) {
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
        return jwtService.generateToken(utente);
    }

    public String registerEmployee(CreateEmployeeDTO request) {
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
        return jwtService.generateToken(dipendente);
    }



    public String login(LoginRequestDTO request) {
        // 1. Autentica le credenziali dell'utente (email e password).
        //    Se sono errate, questo metodo lancerà un'eccezione che
        //    verrà gestita dal tuo AuthenticationController.
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        // 2. Se l'autenticazione ha successo, recupera l'utente completo dal database.
        //    Questo oggetto 'user' contiene l'ID e tutti gli altri dati necessari.
        var user = repository.findByEmail(request.email())
                .orElseThrow(() -> new UsernameNotFoundException("Email non registrata."));

        // 3. Genera il token passando l'intera entità 'Utente'.
        //    Questo invoca il metodo corretto nel tuo JwtService, che è stato
        //    progettato appositamente per aggiungere il 'userId' ai claims del token.
        return jwtService.generateToken(user);
    }

}