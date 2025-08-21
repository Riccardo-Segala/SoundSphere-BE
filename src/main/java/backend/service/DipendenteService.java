package backend.service;

import backend.dto.dipendente.ResponseEmployeeDTO;
import backend.mapper.EmployeeMapper;
import backend.model.Dipendente;
import backend.repository.DipendenteRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DipendenteService extends GenericService<Dipendente, UUID> {

    private final DipendenteRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    public DipendenteService(DipendenteRepository repository, DipendenteRepository employeeRepository, EmployeeMapper employeeMapper) {
        super(repository); // Passa il repository al costruttore della classe base
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
    }

    public ResponseEmployeeDTO getEmployeeDetailsById(UUID id) {
        Dipendente dipendente = employeeRepository.findDipendenteByIdWithDetails(id)
                .orElseThrow(() -> new RuntimeException("Dipendente non trovato"));
        return employeeMapper.toDto(dipendente);
    }

}
