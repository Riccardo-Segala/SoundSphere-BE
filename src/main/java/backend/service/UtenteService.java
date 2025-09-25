package backend.service;

import backend.dto.utente.ResponseUserDTO;
import backend.dto.utente.UpdateUserDTO;
import backend.dto.utente.admin.CreateUserFromAdminDTO;
import backend.dto.utente.admin.UpdateUserFromAdminDTO;
import backend.exception.ResourceNotFoundException;
import backend.mapper.UserMapper;
import backend.mapper.resolver.RoleResolver;
import backend.model.*;
import backend.model.enums.Tipologia;
import backend.repository.NoleggioRepository;
import backend.repository.OrdineRepository;
import backend.repository.UtenteRepository;
import backend.repository.UtenteRuoloRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Value;
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
    private final OrdineRepository ordineRepository;
    private final DatiStaticiService datiStaticiService;
    private final NoleggioRepository noleggioRepository ;

    @Value("${app.static-data.delivery-cost}")
    private String deliveryCostName;

    public UtenteService(UtenteRepository repository, UserMapper userMapper, DipendenteService dipendenteService, UtenteRepository userRepository, UtenteRuoloRepository utenteRuoloRepository, PasswordEncoder passwordEncoder, RuoloService ruoloService, VantaggioService vantaggioService, UtenteRuoloService utenteRuoloService, RoleResolver roleResolver, EntityManager entityManager, OrdineRepository ordineRepository, DatiStaticiService datiStaticiService, NoleggioRepository noleggioRepository) {
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
        this.ordineRepository = ordineRepository;
        this.datiStaticiService = datiStaticiService;
        this.noleggioRepository = noleggioRepository;
    }


    @Transactional(readOnly = true)
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

        Vantaggio vantaggio = vantaggioService.findVantaggioByPunti(utente.getPunti());
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

    public boolean existsById(UUID userId) {
        return userRepository.existsById(userId);
    }


    @Transactional
    public int updatePointsAndAdvantagesForOrder(UUID userId, UUID orderId) {
        // 1. Recupera le entità e valida la loro relazione
        Utente user = findAndValidateUser(userId);
        Ordine order = findAndValidateOrder(orderId, user);

        // 2. Esegue il calcolo dei punti
        int puntiGuadagnati = calculatePointsForOrder(order);

        // 3. Applica i punti e il nuovo vantaggio all'utente
        applyPointsAndAdvantageToUser(user, puntiGuadagnati);

        // 4. Salva e restituisce l'utente aggiornato
        Utente updatedUser = userRepository.save(user);

        return updatedUser.getPunti();
    }

    @Transactional
    public int updatePointsAndAdvantagesForRental(UUID userId, UUID rentalId) {
        // 1. Recupera le entità e valida la loro relazione
        Utente user = findAndValidateUser(userId);
        Noleggio rental = findAndValidateRenral(rentalId, user);

        // 2. Esegue il calcolo dei punti
        int puntiGuadagnati = (int) Math.round(rental.getTotale() * 0.10);

        // 3. Applica i punti e il nuovo vantaggio all'utente
        applyPointsAndAdvantageToUser(user, puntiGuadagnati);

        // 4. Salva e restituisce l'utente aggiornato
        Utente updatedUser = userRepository.save(user);

        return updatedUser.getPunti();
    }

    private Noleggio findAndValidateRenral(UUID rentalId, Utente user) {
        Noleggio rental = noleggioRepository.findById(rentalId)
                .orElseThrow(() -> new EntityNotFoundException("Noleggio non trovato con id: " + rentalId));

        if (!rental.getUtente().getId().equals(user.getId())) {
            throw new IllegalStateException("Il noleggio non appartiene all'utente specificato.");
        }
        return rental;
    }

    private Utente findAndValidateUser(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Utente non trovato con id: " + userId));
    }

    /**
     * Trova un ordine e valida che appartenga all'utente specificato.
     */
    private Ordine findAndValidateOrder(UUID ordineId, Utente utente) {
        Ordine ordine = ordineRepository.findById(ordineId)
                .orElseThrow(() -> new EntityNotFoundException("Ordine non trovato con id: " + ordineId));

        if (!ordine.getUtente().getId().equals(utente.getId())) {
            throw new IllegalStateException("L'ordine non appartiene all'utente specificato.");
        }
        return ordine;
    }

    /**
     * Calcola i punti guadagnati. Metodo "puro", non tocca il database.
     * Restituisce il 10% del valore dell'ordine, arrotondato.
     */
    private int calculatePointsForOrder(Ordine order) {
        if(!order.isSpedizioneGratuita())
        {
            int deliveryCost = (int)datiStaticiService.getStaticDataByName(deliveryCostName).valore();
            return (int) Math.round((order.getTotale()-deliveryCost) * 0.10);
        }
        return (int) Math.round(order.getTotale() * 0.10);
    }

    /**
     * Modifica lo stato dell'oggetto Utente aggiungendo i punti e aggiornando il vantaggio.
     */
    private void applyPointsAndAdvantageToUser(Utente utente, int pointsToAdd) {
        int newScore = utente.getPunti() + pointsToAdd;
        utente.setPunti(newScore);

        Vantaggio newBenefit = vantaggioService.findVantaggioByPunti(newScore);

        if(utente.getVantaggio() != newBenefit)
        {
            utente.setVantaggio(newBenefit);
        }
    }
}