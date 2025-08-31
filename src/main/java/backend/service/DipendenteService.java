package backend.service;

import backend.dto.dipendente.ResponseEmployeeDTO;
import backend.dto.dipendente.admin.CreateEmployeeFromAdminDTO;
import backend.dto.dipendente.admin.UpdateEmployeeFromAdminDTO;
import backend.dto.filiale.ResponseBranchDTO;
import backend.exception.ResourceNotFoundException;
import backend.mapper.BranchMapper;
import backend.mapper.EmployeeMapper;
import backend.model.Dipendente;
import backend.model.Filiale;
import backend.repository.DipendenteRepository;
import backend.repository.UtenteRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DipendenteService extends GenericService<Dipendente, UUID> {

    private final DipendenteRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final BranchMapper branchMapper;
    private final FilialeService filialeService;
    private final PasswordEncoder passwordEncoder;
    private final UtenteRepository utenteRepository;

    public DipendenteService(DipendenteRepository repository, DipendenteRepository employeeRepository, EmployeeMapper employeeMapper, BranchMapper branchMapper, FilialeService filialeService, PasswordEncoder passwordEncoder, UtenteRepository utenteRepository) {
        super(repository); // Passa il repository al costruttore della classe base
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
        this.branchMapper = branchMapper;
        this.filialeService = filialeService;
        this.passwordEncoder = passwordEncoder;
        this.utenteRepository = utenteRepository;
    }

    public ResponseEmployeeDTO getEmployeeDetailsById(UUID id) {
        Dipendente dipendente = employeeRepository.findDipendenteByIdWithDetails(id)
                .orElseThrow(() -> new RuntimeException("Dipendente non trovato"));
        return employeeMapper.toDto(dipendente);
    }

    public ResponseBranchDTO getMyBranch(UUID id) {
        Dipendente dipendente = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dipendente non trovato"));
        Filiale filiale = dipendente.getFiliale();
        if (filiale == null) {
            throw new ResourceNotFoundException("Il dipendente non è associato a nessuna filiale");
        }

        // 4. Esegui il mapping qui e restituisci il DTO
        return branchMapper.toDto(filiale);
    }

    public List<ResponseEmployeeDTO> findAllEmployees() {
        return employeeRepository.findAll().stream()
                .map(employeeMapper::toDto)
                .collect(Collectors.toList());
    }

    public ResponseEmployeeDTO findEmployeeById(UUID id) {
        Dipendente employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utente non trovato con id: " + id));
        return employeeMapper.toDto(employee);
    }

    @Transactional
    public ResponseEmployeeDTO createEmployee(CreateEmployeeFromAdminDTO dto) {
        // 1. Validazione preliminare dei dati
        if (utenteRepository.existsByEmail(dto.utente().email())) {
            throw new IllegalArgumentException("Email già in uso.");
        }
        filialeService.findById(dto.filialeId())
                .orElseThrow(() -> new ResourceNotFoundException("Filiale non trovata con id: " + dto.filialeId()));

        // 2. Mappatura dal DTO all'entità usando MapStruct
        Dipendente dipendente = employeeMapper.fromAdminCreateDto(dto);

        // 3. Logica di Business: Imposta valori che non arrivano dal DTO
        // Codifica la password prima di salvarla
        dipendente.setPassword(passwordEncoder.encode(dto.utente().password()));

        // 4. Salva l'entità nel database
        Dipendente savedDipendente = employeeRepository.save(dipendente);

        // 5. Ritorna il DTO di risposta mappando l'entità appena salvata
        return employeeMapper.toDto(savedDipendente);
    }

    public ResponseEmployeeDTO updateEmployee(UUID id, UpdateEmployeeFromAdminDTO dto) {
        // 1. Recupera l'entità esistente dal database
        Dipendente dipendente = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dipendente non trovato con id: " + id));

        // 2. Applica le modifiche parziali
        employeeMapper.partialUpdateFromAdminUpdate(dto, dipendente);

        // 3. Logica di Business: Gestione speciale per la password
        String newPassword = dto.password();
        if (newPassword != null && !newPassword.trim().isEmpty()) {
            dipendente.setPassword(passwordEncoder.encode(newPassword));
        }

        // 4. Salva l'entità aggiornata
        Dipendente updatedDipendente = employeeRepository.save(dipendente);

        // 5. Ritorna il DTO di risposta aggiornato
        return employeeMapper.toDto(updatedDipendente);
    }
}
