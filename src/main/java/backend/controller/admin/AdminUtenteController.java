package backend.controller.admin;

import backend.dto.utente.DeleteUsersDTO;
import backend.dto.utente.ResponseUserDTO;
import backend.dto.utente.UserIdListDTO;
import backend.dto.utente.admin.CreateUserFromAdminDTO;
import backend.dto.utente.admin.UpdateUserFromAdminDTO;
import backend.service.UtenteService;
import jakarta.validation.Valid;
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
    private final UtenteService userService;

    @GetMapping
    public ResponseEntity<List<ResponseUserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.findAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseUserDTO> getUserById(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.findUserById(id));
    }

    @PostMapping
    public ResponseEntity<ResponseUserDTO> createUser(@RequestBody CreateUserFromAdminDTO utente) {
        // Qui l'admin può creare un utente base, specificando tutti i campi.
        return ResponseEntity.ok(userService.createUser(utente));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseUserDTO> updateUser(@PathVariable UUID id, @RequestBody UpdateUserFromAdminDTO utenteDetails) {
        return ResponseEntity.ok(userService.updateUser(id, utenteDetails));
    }

    @PostMapping("/promote-to-organizer") // URL specifico per l'azione di business
    public ResponseEntity<Void> promoteUsersToOrganizers(
            @Valid @RequestBody UserIdListDTO dto) {

        // Il controller delega tutta la logica complessa al service.
        userService.assignEventManagerRole(dto.userIdList());

        // Restituisce una risposta positiva con corpo vuoto.
        return ResponseEntity.ok().build();
    }

    @PostMapping("/demote-to-user") // URL specifico per l'azione di business
    public ResponseEntity<Void> demoteOrganizersToUsers(
            @Valid @RequestBody UserIdListDTO dto) {

        // Il controller delega tutta la logica complessa al service.
        userService.demoteUsersFromEventManager(dto.userIdList());

        // Restituisce una risposta positiva con corpo vuoto.
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        return null;
    }

    @DeleteMapping("/users")
    public ResponseEntity<Void> deleteUsers(@RequestBody DeleteUsersDTO dto) {
        userService.deleteUsers(dto.userIds());
        return null;
    }
}
