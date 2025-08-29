package backend.service;

import backend.dto.utente.ResponseUserDTO;
import backend.dto.utente.UpdateUserDTO;
import backend.mapper.UserMapper;
import backend.model.Utente;
import backend.repository.UtenteRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UtenteService extends GenericService<Utente, UUID> {

    private final UserMapper userMapper;
    private final DipendenteService dipendenteService;
    private final UtenteRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UtenteService(UtenteRepository repository, UserMapper userMapper, DipendenteService dipendenteService, UtenteRepository userRepository, PasswordEncoder passwordEncoder) {
        super(repository); // Passa il repository al costruttore della classe base
        this.userMapper = userMapper;
        this.dipendenteService = dipendenteService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Transactional(readOnly = true) // Le operazioni di lettura dovrebbero essere transazionali
    public Object getUserDetailsById(UUID userId, boolean isDipendente) {
        if (isDipendente) {
            return dipendenteService.getEmployeeDetailsById(userId);
        } else {
            Utente utente = userRepository.findUtenteByIdWithDetails(userId)
                    .orElseThrow(() -> new RuntimeException("Utente non trovato"));
            return userMapper.toDto(utente);
        }
    }
    @Transactional
    public ResponseUserDTO updateCurrentUser(UUID userId, UpdateUserDTO dto) {
        Utente existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

        // Mappa i campi aggiornati dal DTO all'entità esistente
        userMapper.partialUpdateFromUpdate(dto, existingUser);

        String newPassword = dto.password();
        if (newPassword != null && !newPassword.trim().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(newPassword));
        }

        // Salva l'entità aggiornata nel database
        Utente updatedUser = userRepository.save(existingUser);

        // Mappa l'entità aggiornata al DTO di risposta
        return userMapper.toDto(updatedUser);

    }

    public boolean existsById(UUID userId) {
        return userRepository.existsById(userId);
    }


}