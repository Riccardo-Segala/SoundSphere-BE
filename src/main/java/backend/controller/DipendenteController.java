package backend.controller;

import backend.dto.dipendente.CreateEmployeeDTO;
import backend.dto.dipendente.ResponseEmployeeDTO;
import backend.dto.dipendente.UpdateEmployeeDTO;
import backend.mapper.EmployeeMapper;
import backend.model.Dipendente;
import backend.service.DipendenteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/dipendenti")
class DipendenteController extends GenericController <Dipendente, UUID, CreateEmployeeDTO, UpdateEmployeeDTO, ResponseEmployeeDTO> {
    public DipendenteController(DipendenteService service, EmployeeMapper mapper) {
        super(service, mapper);
    }

    @GetMapping
    public ResponseEntity<List<ResponseEmployeeDTO>> getAllEmployee() {
        return super.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseEmployeeDTO> getEmployeeById(@PathVariable UUID id) {
        return super.getById(id);
    }

    @PostMapping
    public ResponseEntity<ResponseEmployeeDTO> createEmployee(@RequestBody CreateEmployeeDTO createDTO) {
        return super.create(createDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseEmployeeDTO> updateEmployee(@PathVariable UUID id, @RequestBody UpdateEmployeeDTO updateDTO) {
        return super.update(id, updateDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable UUID id) {
        return super.delete(id);
    }

    @Override
    protected UUID getId(Dipendente entity) {
        return entity.getId();
    }

}
