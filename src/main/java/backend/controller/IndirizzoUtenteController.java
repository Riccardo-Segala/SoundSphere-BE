package backend.controller;
import backend.dto.indirizzo_utente.CreateUserAddressDTO;
import backend.dto.indirizzo_utente.ResponseUserAddressDTO;
import backend.dto.indirizzo_utente.UpdateUserAddressDTO;
import backend.mapper.UserAddressMapper;
import backend.model.IndirizzoUtente;
import backend.service.IndirizzoUtenteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/indirizzi-utente")
class IndirizzoUtenteController extends GenericController<IndirizzoUtente, UUID, CreateUserAddressDTO, UpdateUserAddressDTO, ResponseUserAddressDTO> {
    public IndirizzoUtenteController(IndirizzoUtenteService service, UserAddressMapper mapper) {
        super(service, mapper);
    }

    @GetMapping
    public ResponseEntity<List<ResponseUserAddressDTO>> getAllUsersAddress() {
        return super.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseUserAddressDTO> getUserAddressById(@PathVariable UUID id) {
        return super.getById(id);
    }

    @PostMapping
    public ResponseEntity<ResponseUserAddressDTO> createUserAddress(@RequestBody CreateUserAddressDTO createDTO) {
        return super.create(createDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseUserAddressDTO> updateUserAddress(@PathVariable UUID id, @RequestBody UpdateUserAddressDTO updateDTO) {
        return super.update(id, updateDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserAddress(@PathVariable UUID id) {
        return super.delete(id);
    }

    @Override
    protected UUID getId(IndirizzoUtente entity) {
        return entity.getId();
    }
}
