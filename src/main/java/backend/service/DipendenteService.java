package backend.service;

import backend.dto.dipendente.ResponseEmployeeDTO;
import backend.dto.filiale.ResponseBranchDTO;
import backend.exception.ResourceNotFoundException;
import backend.mapper.BranchMapper;
import backend.mapper.EmployeeMapper;
import backend.model.Dipendente;
import backend.model.Filiale;
import backend.repository.DipendenteRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DipendenteService extends GenericService<Dipendente, UUID> {

    private final DipendenteRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final BranchMapper branchMapper;

    public DipendenteService(DipendenteRepository repository, DipendenteRepository employeeRepository, EmployeeMapper employeeMapper, BranchMapper branchMapper) {
        super(repository); // Passa il repository al costruttore della classe base
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
        this.branchMapper = branchMapper;
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
}
