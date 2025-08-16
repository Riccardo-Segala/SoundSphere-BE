package backend.controller;

import backend.dto.utente.CreateUserDTO;
import backend.dto.utente.ResponseUserDTO;
import backend.dto.utente.UpdateUserDTO;
import backend.mapper.UserMapper;
import backend.model.Utente;
import backend.service.UtenteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/utenti")
public class UtenteController extends GenericController<Utente, UUID, CreateUserDTO, UpdateUserDTO, ResponseUserDTO> {
    public UtenteController(UtenteService service, UserMapper mapper) {
        super(service, mapper);
    }

    @GetMapping
    public ResponseEntity<List<ResponseUserDTO>> getAllUsers() {
        return super.getAll();
    }

    @GetMapping("/{id}")
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