package backend.controller;

import backend.dto.utente.CreateUserDTO;
import backend.dto.utente.ResponseUserDTO;
import backend.dto.utente.UpdateUserDTO;
import backend.mapper.EmployeeMapper;
import backend.mapper.UserMapper;
import backend.model.Utente;
import backend.security.CustomUserDetails;
import backend.service.UtenteService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path="/api/utenti", produces = MediaType.APPLICATION_JSON_VALUE)
public class UtenteController extends GenericController<Utente, UUID, CreateUserDTO, UpdateUserDTO, ResponseUserDTO> {
    private final EmployeeMapper employeeMapper;
    private final UserMapper userMapper;
    private final UtenteService userService;

    public UtenteController(UtenteService service, UserMapper mapper, EmployeeMapper employeeMapper, UtenteService userService) {
        super(service, mapper);
        this.employeeMapper = employeeMapper;
        this.userMapper = mapper;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<ResponseUserDTO>> getAllUsers() {
        return super.getAll();
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal CustomUserDetails userDetails) {
        // Controlla il ruolo chiamando .getAuthorities() direttamente sul principal.
        boolean isDipendente = userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_DIPENDENTE"));

        // Prende l'ID direttamente, senza bisogno di getPrincipal() o cast.
        UUID userId = userDetails.getId();

        // 2. chiama il service
        Object dto = userService.getUserDetailsById(userId, isDipendente);

        // 3. restituisce la risposta
        return ResponseEntity.ok(dto);
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