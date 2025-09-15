package backend.service;

import backend.dto.utente.ResponseUserDTO;
import backend.dto.utente.UpdateUserDTO;
import backend.dto.utente.admin.CreateUserFromAdminDTO;
import backend.dto.utente.admin.UpdateUserFromAdminDTO;
import backend.exception.ResourceNotFoundException;
import backend.mapper.UserMapper;
import backend.mapper.resolver.RoleResolver;
import backend.model.Ruolo;
import backend.model.Utente;
import backend.model.Vantaggio;
import backend.model.enums.Tipologia;
import backend.repository.UtenteRepository;
import backend.repository.UtenteRuoloRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.access.AccessDeniedException;
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
    private final UtenteRuoloRepository utenteRuoloRepository;
    private final PasswordEncoder passwordEncoder;
    private final RuoloService ruoloService;
    private final VantaggioService vantaggioService;
    private final UtenteRuoloService utenteRuoloService;
    private final RoleResolver roleResolver;
    private final EntityManager entityManager;

    public UtenteService(UtenteRepository repository, UserMapper userMapper, DipendenteService dipendenteService, UtenteRepository userRepository, UtenteRuoloRepository utenteRuoloRepository, PasswordEncoder passwordEncoder, RuoloService ruoloService, VantaggioService vantaggioService, UtenteRuoloService utenteRuoloService, RoleResolver roleResolver, EntityManager entityManager) {
        super(repository); // Passa il repository al costruttore della classe base
        this.userMapper = userMapper;
        this.dipendenteService = dipendenteService;
        this.userRepository = userRepository;
        this.utenteRuoloRepository = utenteRuoloRepository;
        this.passwordEncoder = passwordEncoder;
        this.ruoloService = ruoloService;
        this.vantaggioService = vantaggioService;
        this.utenteRuoloService = utenteRuoloService;
        this.roleResolver = roleResolver;
        this.entityManager = entityManager;
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

        Vantaggio vantaggio = vantaggioService.findById(dto.vantaggioId())
                .orElseThrow(() -> new IllegalStateException("Vantaggio default non trovato"));
        utente.setVantaggio(vantaggio);

        // 5. Salva l'entità nel database
        Utente savedUtente = userRepository.save(utente);

        // Assegna il ruolo "UTENTE"
        if(dto.ruoliIds() != null) {
            Set<Ruolo> ruoli = roleResolver.findRolesByIds(dto.ruoliIds());
            utenteRuoloService.handleRoleTransition(List.of(savedUtente), List.copyOf(ruoli));
        }
        else {
            Ruolo ruoloUtente = ruoloService.findByName("UTENTE");
            utenteRuoloService.handleRoleTransition(List.of(savedUtente), List.of(ruoloUtente));
        }

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
    public void assignEventManagerRole(List<UUID> userIds) {
        if (CollectionUtils.isEmpty(userIds)) {
            return;
        }

        // 1. Carica i ruoli di riferimento
        Ruolo eventManagerRole = ruoloService.findByName("ORGANIZZATORE_EVENTI");

        // 2. Carica e valida gli utenti
        List<Utente> users = userRepository.findAllById(userIds);
        if (users.size() != userIds.size()) {
            throw new ResourceNotFoundException("Uno o più utenti non sono stati trovati.");
        }

        // 3. Delega la logica complessa al metodo privato
        utenteRuoloService.handleRoleTransition(users, List.of(eventManagerRole));
    }
    @Transactional
    public void demoteUsersFromEventManager(List<UUID> userIds) {
        if (CollectionUtils.isEmpty(userIds)) {
            return;
        }

        // 1. Carica i ruoli di riferimento
        Ruolo userRoleToAdd = ruoloService.findByName("UTENTE");

        // 2. Carica e valida gli utenti
        List<Utente> users = userRepository.findAllById(userIds);
        if (users.size() != userIds.size()) {
            throw new ResourceNotFoundException("Uno o più utenti non sono stati trovati.");
        }

        // 3. Delega la logica complessa al metodo privato
        utenteRuoloService.handleRoleTransition(users, List.of(userRoleToAdd));
    }
    // metodi di utilità

    public boolean existsById(UUID userId) {
        return userRepository.existsById(userId);
    }

    // Verifica che un utente esista e possieda un ruolo specifico e attivo.
    // Se la validazione fallisce, lancia un'eccezione.
    @Transactional
    public Utente findAndValidateUserWithRole(UUID utenteId, String nomeRuolo) {
        // 1. Trova l'utente o lancia l'eccezione se non esiste.
        Utente utente = userRepository.findById(utenteId)
                .orElseThrow(() -> new EntityNotFoundException("Utente non trovato con ID: " + utenteId));

        // 2. Controlla se tra i ruoli assegnati ce n'è uno che corrisponde al nome richiesto
        //    e che non sia scaduto.
        boolean hasValidRole = utente.getUtenteRuoli().stream()
                .anyMatch(assegnazione ->
                        assegnazione.getRuolo() != null &&
                        // Condizione 1: Il nome del ruolo deve corrispondere
                        assegnazione.getRuolo().getNome().equals(nomeRuolo) &&

                                // Condizione 2: La data di scadenza non deve esistere (null) OPPURE deve essere futura
                                (assegnazione.getDataScadenza() == null || assegnazione.getDataScadenza().isAfter(LocalDate.now()))
                );

        // 3. Se nessun ruolo valido è stato trovato, lancia un'eccezione di accesso negato.
        if (!hasValidRole) {
            throw new AccessDeniedException("L'utente non ha il ruolo necessario (" + nomeRuolo + ") per eseguire questa operazione.");
        }

        // 4. Se tutto è ok, restituisci l'utente.
        return utente;
    }


}