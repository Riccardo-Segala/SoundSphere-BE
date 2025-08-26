package backend.controller;
import backend.dto.indirizzo_utente.CreateUserAddressDTO;
import backend.dto.indirizzo_utente.ResponseUserAddressDTO;
import backend.dto.indirizzo_utente.UpdateUserAddressDTO;
import backend.mapper.UserAddressMapper;
import backend.model.IndirizzoUtente;
import backend.security.CustomUserDetails;
import backend.service.IndirizzoUtenteService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path="/api/indirizzi-utente", produces = MediaType.APPLICATION_JSON_VALUE)
class IndirizzoUtenteController extends GenericController<IndirizzoUtente, UUID, CreateUserAddressDTO, UpdateUserAddressDTO, ResponseUserAddressDTO> {
    private final IndirizzoUtenteService indirizzoService;
    public IndirizzoUtenteController(IndirizzoUtenteService service, UserAddressMapper mapper) {
        super(service, mapper);
        this.indirizzoService = service;
    }

    /*@GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ResponseUserAddressDTO>> getAllUsersAddressForAdmin() {
        return super.getAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseUserAddressDTO> getUserAddressByIdForAdmin(@PathVariable UUID id) {
        return super.getById(id);
    }*/

    @GetMapping("/personalUserAddresses")
    public ResponseEntity<List<ResponseUserAddressDTO>> getAllUserAddressesByUserId(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        UUID userId = userDetails.getId(); // Ottiene l'ID dell'utente autenticato

        List<ResponseUserAddressDTO> indirizzi = indirizzoService.getAllUserAddressByUserId(userId);
        return ResponseEntity.ok(indirizzi);
    }

    @PostMapping
    public ResponseEntity<ResponseUserAddressDTO> createUserAddress(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody CreateUserAddressDTO createDTO) {
        UUID userId = userDetails.getId(); // Ottiene l'ID dell'utente autenticato

        ResponseUserAddressDTO nuovoIndirizzoDto = indirizzoService.createForUser(userId, createDTO);

        // Costruisce l'URI della nuova risorsa creata
        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/{id}")
                .buildAndExpand(nuovoIndirizzoDto.id())
                .toUri();

        // Restituisce 201 Created con l'header Location e il body della risorsa
        return ResponseEntity.created(location).body(nuovoIndirizzoDto);
    }

    @PutMapping("/{addressId}")
    public ResponseEntity<ResponseUserAddressDTO> updateUserAddress(
            @PathVariable UUID addressId,
            @RequestBody UpdateUserAddressDTO updateDTO,
            @AuthenticationPrincipal CustomUserDetails userDetails) { // <-- Ottiene l'utente loggato

        // 1. Estrae l'ID dell'utente dalla sessione di sicurezza.
        UUID userId = userDetails.getId();

        // 2. Chiama il service, passando sia l'ID dell'utente che quello dell'indirizzo.
        ResponseUserAddressDTO indirizzoAggiornato = indirizzoService.updateAddress(userId, addressId, updateDTO);

        // 3. Restituisce 200 OK con il corpo della risorsa aggiornata.
        return ResponseEntity.ok(indirizzoAggiornato);
    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<Void> deleteUserAddress(
            @PathVariable UUID addressId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        // 1. Estrae l'ID dell'utente.
        UUID authUserId = userDetails.getId();

        // 2. Chiama il service per eseguire la cancellazione sicura.
        indirizzoService.deleteAddress(authUserId, addressId);

        // 3. Restituisce 204 No Content
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/{addressId}")
    public ResponseEntity<ResponseUserAddressDTO> getUserAddressById(
            @PathVariable UUID addressId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        UUID userId = userDetails.getId(); // Ottiene l'ID dell'utente autenticato

        ResponseUserAddressDTO indirizzoDto = indirizzoService.findAddressById(userId, addressId);
        return ResponseEntity.ok(indirizzoDto);
    }

    /*@PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseUserAddressDTO> updateUserAddressForAdmin(@PathVariable UUID id, @RequestBody UpdateUserAddressDTO updateDTO) {
        return super.update(id, updateDTO);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUserAddressForAdmin(@PathVariable UUID id) {
        return super.delete(id);
    }*/

    @Override
    protected UUID getId(IndirizzoUtente entity) {
        return entity.getId();
    }
}
