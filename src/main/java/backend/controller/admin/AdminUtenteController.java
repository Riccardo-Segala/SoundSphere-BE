package backend.controller.admin;

import backend.dto.utente.CreateUserDTO;
import backend.dto.utente.ResponseUserDTO;
import backend.dto.utente.UpdateUserDTO;
import backend.service.admin.AdminUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(path="/api/admin/utenti", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminUtenteController {
    private final AdminUserService adminUserService;

    @GetMapping
    public ResponseEntity<List<ResponseUserDTO>> getAllUsers() {
        return ResponseEntity.ok(adminUserService.findAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseUserDTO> getUserById(@PathVariable UUID id) {
        return ResponseEntity.ok(adminUserService.findUserById(id));
    }

    @PostMapping
    public ResponseEntity<ResponseUserDTO> createUser(@RequestBody CreateUserDTO utente) {
        // Qui l'admin può creare un utente base, specificando tutti i campi.
        return ResponseEntity.ok(adminUserService.createUser(utente));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseUserDTO> updateUser(@PathVariable UUID id, @RequestBody UpdateUserDTO utenteDetails) {
        return ResponseEntity.ok(adminUserService.updateUser(id, utenteDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        adminUserService.deleteUser(id);
        return null;
    }
}
