package backend.service.admin;

import backend.dto.utente.CreateUserDTO;
import backend.dto.utente.ResponseUserDTO;
import backend.dto.utente.UpdateUserDTO;
import backend.exception.ResourceNotFoundException;
import backend.mapper.UserMapper;
import backend.model.Ruolo;
import backend.model.Utente;
import backend.model.Vantaggio;
import backend.model.enums.Tipologia;
import backend.repository.UtenteRepository;
import backend.service.RuoloService;
import backend.service.VantaggioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminUserService {
    private final UtenteRepository userRepository;
    private final UserMapper userMapper;
    private final RuoloService ruoloService;
    private final VantaggioService vantaggioService;
    private final PasswordEncoder passwordEncoder;

    public List<ResponseUserDTO> findAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    public ResponseUserDTO findUserById(UUID id) {
        Utente user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utente non trovato con id: " + id));
        return userMapper.toDto(user);
    }

    @Transactional
    public ResponseUserDTO createUser(CreateUserDTO dto) {
        // 1. Controlla se l'email è già in uso
        if (userRepository.existsByEmail(dto.email())) {
            throw new IllegalArgumentException("Email già in uso.");
        }

        // 2. Mappa il DTO in un'entità Utente
        Utente utente = userMapper.fromCreateDto(dto);

        // 3. Codifica la password
        utente.setPassword(passwordEncoder.encode(dto.password()));

        // 4. Imposta valori di default e relazioni
        utente.setDataRegistrazione(LocalDate.now());
        utente.setPunti(0);
        utente.setTipologia(Tipologia.UTENTE);

        Ruolo ruoloUtente = ruoloService.findByName("UTENTE");
        utente.setRuoli(Collections.singleton(ruoloUtente));

        UUID defaultVantaggioId = UUID.fromString("f8c3b3e0-4f5a-4a3c-9c7b-3f1a2b3c4d5e");
        Vantaggio vantaggioDefault = vantaggioService.findById(defaultVantaggioId)
                .orElseThrow(() -> new IllegalStateException("Vantaggio default non trovato"));
        utente.setVantaggio(vantaggioDefault);

        // 5. Salva l'entità nel database
        Utente savedUtente = userRepository.save(utente);

        // 6. Mappa l'entità salvata nel DTO di risposta e la restituisce
        return userMapper.toDto(savedUtente);
    }
    @Transactional
    public ResponseUserDTO updateUser(UUID id, UpdateUserDTO dto) {
        // 1. Trova l'utente esistente
        Utente existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utente non trovato con id: " + id));

        // 2. Aggiorna i campi dall'UpdateUserDTO (logica di mapping parziale)
        userMapper.partialUpdateFromUpdate(dto, existingUser);

        // 3. Gestisce specificamente l'aggiornamento della password (se fornita)
        String newPassword = dto.password();
        if (newPassword != null && !newPassword.trim().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(newPassword));
        }

        // 4. Salva le modifiche
        Utente updatedUser = userRepository.save(existingUser);

        // 5. Mappa nel DTO di risposta e restituisce
        return userMapper.toDto(updatedUser);
    }
    @Transactional
    public void deleteUser(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("Utente non trovato con id: " + id);
        }
        userRepository.deleteById(id);
    }
}
