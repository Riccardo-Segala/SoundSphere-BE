package backend.controller.admin;

import backend.dto.dipendente.ResponseEmployeeDTO;
import backend.dto.dipendente.admin.CreateEmployeeFromAdminDTO;
import backend.dto.dipendente.admin.UpdateEmployeeFromAdminDTO;
import backend.service.DipendenteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(path="/api/admin/dipendente", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminDipendenteController {
    private final DipendenteService employeeService;
    @GetMapping
    public ResponseEntity<List<ResponseEmployeeDTO>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.findAllEmployees());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseEmployeeDTO> getEmployeeById(@PathVariable UUID id) {
        return ResponseEntity.ok(employeeService.findEmployeeById(id));
    }

    @PostMapping
    public ResponseEntity<ResponseEmployeeDTO> createEmployee(@RequestBody CreateEmployeeFromAdminDTO utente) {
        return ResponseEntity.ok(employeeService.createEmployee(utente));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseEmployeeDTO> updateEmployee(@PathVariable UUID id, @RequestBody UpdateEmployeeFromAdminDTO utenteDetails) {
        return ResponseEntity.ok(employeeService.updateEmployee(id, utenteDetails));
    }
}
