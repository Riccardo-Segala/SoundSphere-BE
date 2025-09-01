package backend.service;

import backend.dto.utente.ResponseUserDTO;
import backend.dto.utente.RoleAssignmentDTO;
import backend.dto.utente.UpdateUserDTO;
import backend.dto.utente.admin.CreateUserFromAdminDTO;
import backend.dto.utente.admin.UpdateUserFromAdminDTO;
import backend.exception.ResourceNotFoundException;
import backend.mapper.UserMapper;
import backend.model.Ruolo;
import backend.model.Utente;
import backend.model.UtenteRuolo;
import backend.model.Vantaggio;
import backend.model.enums.Tipologia;
import backend.repository.UtenteRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UtenteService extends GenericService<Utente, UUID> {

    private final UserMapper userMapper;
    private final DipendenteService dipendenteService;
    private final UtenteRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RuoloService ruoloService;
    private final VantaggioService vantaggioService;

    public UtenteService(UtenteRepository repository, UserMapper userMapper, DipendenteService dipendenteService, UtenteRepository userRepository, PasswordEncoder passwordEncoder, RuoloService ruoloService, VantaggioService vantaggioService) {
        super(repository); // Passa il repository al costruttore della classe base
        this.userMapper = userMapper;
        this.dipendenteService = dipendenteService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.ruoloService = ruoloService;
        this.vantaggioService = vantaggioService;
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
    public ResponseUserDTO createUser(CreateUserFromAdminDTO dto) {
        // 1. Controlla se l'email è già in uso
        if (userRepository.existsByEmail(dto.email())) {
            throw new IllegalArgumentException("Email già in uso.");
        }

        // 2. Mappa il DTO in un'entità Utente
        Utente utente = userMapper.fromAdminCreateDto(dto);

        // 3. Codifica la password
        utente.setPassword(passwordEncoder.encode(dto.password()));

        // 4. Imposta valori di default e relazioni
        utente.setDataRegistrazione(LocalDate.now());
        utente.setPunti(0);
        utente.setTipologia(Tipologia.UTENTE);

        // Assegna il ruolo "UTENTE"
        Ruolo ruoloUtente = ruoloService.findByName("UTENTE");
        UtenteRuolo defaultAssignment = new UtenteRuolo();
        defaultAssignment.setUtente(utente);
        defaultAssignment.setRuolo(ruoloUtente);
        utente.getUtenteRuoli().add(defaultAssignment);

        Vantaggio vantaggio = vantaggioService.findById(dto.vantaggioId())
                .orElseThrow(() -> new IllegalStateException("Vantaggio default non trovato"));
        utente.setVantaggio(vantaggio);

        // 5. Salva l'entità nel database
        Utente savedUtente = userRepository.save(utente);

        // 6. Mappa l'entità salvata nel DTO di risposta e la restituisce
        return userMapper.toDto(savedUtente);
    }
    @Transactional
    public ResponseUserDTO updateUser(UUID id, UpdateUserFromAdminDTO dto) {
        // 1. Trova l'utente esistente
        Utente existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utente non trovato con id: " + id));

        // 2. Aggiorna i campi dall'UpdateUserDTO (logica di mapping parziale)
        userMapper.partialUpdateFromAdminUpdate(dto, existingUser);

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

    @Transactional
    public void deleteUsers(List<UUID> userIds) {
        List<Utente> usersToDelete = userRepository.findAllById(userIds);

        if (usersToDelete.size() != userIds.size()) {
            throw new ResourceNotFoundException("Uno o più utenti non trovati per gli ID forniti.");
        }

        userRepository.deleteAll(usersToDelete);
    }

    @Transactional
    public void assignEventManagerRole(List<RoleAssignmentDTO> assignments) {
        if (CollectionUtils.isEmpty(assignments)) {
            return;
        }

        // 1. ORCHESTRAZIONE: Recupera il ruolo una sola volta
        Ruolo ruoloOrganizzatore = ruoloService.findByName("ORGANIZZATORE_EVENTI");

        // 2. ORCHESTRAZIONE: Recupera tutti gli utenti in una sola query
        Set<UUID> utenteIds = assignments.stream().map(RoleAssignmentDTO::userId).collect(Collectors.toSet());
        List<Utente> utentiTrovati = userRepository.findAllById(utenteIds);

        // 3. VALIDAZIONE: Controlla che tutti gli utenti esistano
        if (utentiTrovati.size() != utenteIds.size()) {
            throw new ResourceNotFoundException("Uno o più utenti non sono stati trovati.");
        }

        // Mappa per efficienza
        Map<UUID, RoleAssignmentDTO> mappaAssignments = assignments.stream()
                .collect(Collectors.toMap(RoleAssignmentDTO::userId, dto -> dto));

        // 4. DELEGA: Itera e chiama il mapper per ogni utente
        for (Utente utente : utentiTrovati) {
            RoleAssignmentDTO assignmentDto = mappaAssignments.get(utente.getId());
            userMapper.updateRoleFromDto(assignmentDto, ruoloOrganizzatore, utente); // <-- LOGICA DELEGATA
        }
    }

    // metodi di utilità

    public boolean existsById(UUID userId) {
        return userRepository.existsById(userId);
    }
}