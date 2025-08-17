package backend.controller;

import backend.dto.utente.CreateUserDTO;
import backend.dto.utente.ResponseUserDTO;
import backend.dto.utente.UpdateUserDTO;
import backend.mapper.EmployeeMapper;
import backend.mapper.UserMapper;
import backend.model.Dipendente;
import backend.model.Utente;
import backend.service.UtenteService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/utenti")
public class UtenteController extends GenericController<Utente, UUID, CreateUserDTO, UpdateUserDTO, ResponseUserDTO> {
    private final EmployeeMapper employeeMapper;
    private final UserMapper userMapper;

    public UtenteController(UtenteService service, UserMapper mapper, EmployeeMapper employeeMapper) {
        super(service, mapper);
        this.employeeMapper = employeeMapper;
        this.userMapper = mapper;
    }

    @GetMapping
    public ResponseEntity<List<ResponseUserDTO>> getAllUsers() {
        return super.getAll();
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        // Spring Security, grazie al nostro filtro, fornisce l'oggetto Authentication
        // che contiene l'utente completo come "principal".
        Utente currentUser = (Utente) authentication.getPrincipal();

        // CONTROLLO DEL TIPO
        if (currentUser instanceof Dipendente) {
            // Se è un Dipendente, usa il DipendenteMapper per ottenere il DTO completo
            Dipendente dipendente = (Dipendente) currentUser;
            var dto = employeeMapper.toDto(dipendente);
            return ResponseEntity.ok(dto);
        } else {
            // Altrimenti, usa il UserMapper standard
            var dto = userMapper.toDto(currentUser);
            return ResponseEntity.ok(dto);
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isOwner(authentication, #id)")
    public ResponseEntity<ResponseUserDTO> getUserById(@PathVariable UUID id) {
        return super.getById(id);
    }

    @PostMapping
    public ResponseEntity<ResponseUserDTO> createUser(@RequestBody CreateUserDTO createDTO) {
        return super.create(createDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseUserDTO> updateUser(@PathVariable UUID id, @RequestBody UpdateUserDTO updateDTO) {
        return super.update(id, updateDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        return super.delete(id);
    }

    @Override
    protected UUID getId(Utente entity) {
        return entity.getId();
    }
}