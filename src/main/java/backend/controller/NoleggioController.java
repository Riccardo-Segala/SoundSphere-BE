package backend.controller;

import backend.dto.checkout.CheckoutInputRentalDTO;
import backend.dto.checkout.CheckoutOutputRentalDTO;
import backend.dto.noleggio.CreateRentalDTO;
import backend.dto.noleggio.ResponseRentalDTO;
import backend.dto.noleggio.UpdateRentalDTO;
import backend.mapper.RentalMapper;
import backend.model.Noleggio;
import backend.security.CustomUserDetails;
import backend.service.NoleggioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path="/api/noleggi", produces = MediaType.APPLICATION_JSON_VALUE)
class NoleggioController extends GenericController<Noleggio, UUID, CreateRentalDTO, UpdateRentalDTO, ResponseRentalDTO> {
    public NoleggioController(NoleggioService service, RentalMapper mapper) {
        super(service, mapper);
    }

    @Autowired
    private NoleggioService noleggioService;

    @GetMapping
    public ResponseEntity<List<ResponseRentalDTO>> getAllRentals() {
        return super.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseRentalDTO> getRentalById(@PathVariable UUID id) {
        return super.getById(id);
    }

    @PostMapping
    public ResponseEntity<ResponseRentalDTO> createRental(@RequestBody CreateRentalDTO createDTO) {
        return super.create(createDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseRentalDTO> updateRental(@PathVariable UUID id, @RequestBody UpdateRentalDTO updateDTO) {
        return super.update(id, updateDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRental(@PathVariable UUID id) {
        return super.delete(id);
    }

    @Override
    protected UUID getId(Noleggio entity) {
        return entity.getId();
    }

    @PostMapping("/checkout")
    @PreAuthorize("hasAuthority('NOLEGGIO')") // 2. Permesso specifico per il noleggio
    public ResponseEntity<CheckoutOutputRentalDTO> noleggia(
            @RequestBody CheckoutInputRentalDTO noleggioDto, // 3. DTO di input per il noleggio
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        UUID utenteId = userDetails.getId();

        CheckoutOutputRentalDTO noleggioConfermato = noleggioService.checkoutNoleggio(noleggioDto, utenteId);

        return new ResponseEntity<>(noleggioConfermato, HttpStatus.CREATED);
    }

    // --- METODO PER LA STORIA NOLEGGI DELL'ORGANIZZATORE ---
    @GetMapping("/miei-noleggi")
    public ResponseEntity<List<ResponseRentalDTO>> getMyRentals(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        // 1. Recupera l'ID utente in modo sicuro dal token
        UUID utenteId = userDetails.getId();

        // 2. Chiama il service per ottenere la lista di DTO
        List<ResponseRentalDTO> mieiNoleggi = noleggioService.findRentalsByUserId(utenteId);

        // 3. Restituisci la lista
        return ResponseEntity.ok(mieiNoleggi);
    }

}
