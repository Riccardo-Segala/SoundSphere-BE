package backend.controller;

import backend.dto.dipendente.CreateEmployeeDTO;
import backend.dto.dipendente.ResponseEmployeeDTO;
import backend.dto.dipendente.UpdateEmployeeDTO;
import backend.dto.filiale.ResponseBranchDTO;
import backend.mapper.EmployeeMapper;
import backend.model.Dipendente;
import backend.security.CustomUserDetails;
import backend.service.DipendenteService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path="/api/dipendenti", produces = MediaType.APPLICATION_JSON_VALUE)
class DipendenteController extends GenericController <Dipendente, UUID, CreateEmployeeDTO, UpdateEmployeeDTO, ResponseEmployeeDTO> {
    private final DipendenteService dipendenteService;
    private final EmployeeMapper employeeMapper;
    public DipendenteController(DipendenteService service, EmployeeMapper mapper) {
        super(service, mapper);
        this.dipendenteService = service;
        this.employeeMapper = mapper;
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

    @GetMapping("/myBranch")
    public ResponseEntity<ResponseBranchDTO> getMyBranch(@AuthenticationPrincipal CustomUserDetails userDetails) {
        UUID id = userDetails.getId();
        return ResponseEntity.ok(dipendenteService.getMyBranch(id));
    }

}
