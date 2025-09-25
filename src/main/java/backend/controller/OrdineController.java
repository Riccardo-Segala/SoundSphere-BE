package backend.controller;

import backend.dto.checkout.CheckoutInputDTO;
import backend.dto.checkout.CheckoutOutputDTO;
import backend.dto.ordine.CreateOrderDTO;
import backend.dto.ordine.ResponseOrderDTO;
import backend.dto.ordine.UpdateOrderDTO;
import backend.mapper.OrderMapper;
import backend.model.Ordine;
import backend.security.CustomUserDetails;
import backend.service.OrdineService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path="/api/ordini", produces = MediaType.APPLICATION_JSON_VALUE)
class OrdineController extends GenericController<Ordine, UUID, CreateOrderDTO, UpdateOrderDTO, ResponseOrderDTO> {
    final OrdineService ordineService;
    public OrdineController(OrdineService service, OrderMapper mapper, OrdineService ordineService) {

        super(service, mapper);
        this.ordineService = ordineService;
    }

    @GetMapping
    public ResponseEntity<List<ResponseOrderDTO>> getAllOrders() {
        return super.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseOrderDTO> getOrderById(@PathVariable UUID id) {
        return super.getById(id);
    }

    @PostMapping
    public ResponseEntity<ResponseOrderDTO> createOrder(@RequestBody CreateOrderDTO createDTO) {
        return super.create(createDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseOrderDTO> updateOrder(@PathVariable UUID id, @RequestBody UpdateOrderDTO updateDTO) {
        return super.update(id, updateDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable UUID id) {
        return super.delete(id);
    }

    @Override
    protected UUID getId(Ordine entity) {
        return entity.getId();
    }

    @PostMapping("/checkout")
    @PreAuthorize("hasAuthority('ACQUISTO')")
    public ResponseEntity<CheckoutOutputDTO> checkout(
            @RequestBody CheckoutInputDTO checkoutDto,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {

        UUID utenteId = userDetails.getId();

        // Chiama il service, passando sia i dati della richiesta (DTO)
        // sia l'ID dell'utente autenticato
        CheckoutOutputDTO ordineConfermato = ordineService.checkout(checkoutDto, utenteId);

        // Restituisce una risposta di successo con il DTO di output e lo status 201 Created
        return new ResponseEntity<>(ordineConfermato, HttpStatus.CREATED);
    }

    // --- METODO PER LO STORICO ORDINI DELL'UTENTE ---
    @GetMapping("/miei-ordini")
    public ResponseEntity<List<ResponseOrderDTO>> getMyOrders(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        // Recupera l'ID utente in modo sicuro dal token
        UUID utenteId = userDetails.getId();

        // Chiama il service per ottenere la lista di DTO
        List<ResponseOrderDTO> mieiOrdini = ordineService.findOrdersByUserId(utenteId);

        // Restituisci la lista
        return ResponseEntity.ok(mieiOrdini);
    }


}
