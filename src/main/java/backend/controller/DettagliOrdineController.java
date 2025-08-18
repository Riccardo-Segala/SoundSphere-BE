package backend.controller;

import backend.dto.dettagli_ordine.CreateOrderDetailsDTO;
import backend.dto.dettagli_ordine.ResponseOrderDetailsDTO;
import backend.dto.dettagli_ordine.UpdateOrderDetailsDTO;
import backend.mapper.OrderDetailsMapper;
import backend.model.DettagliOrdine;
import backend.model.embeddable.OrdineProdottoId;
import backend.service.DettagliOrdineService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path="/api/dettagli-ordini", produces = MediaType.APPLICATION_JSON_VALUE)
class DettagliOrdineController extends GenericController<DettagliOrdine, OrdineProdottoId, CreateOrderDetailsDTO, UpdateOrderDetailsDTO, ResponseOrderDetailsDTO> {
    public DettagliOrdineController(DettagliOrdineService service, OrderDetailsMapper mapper) {
        super(service, mapper);
    }

    @GetMapping
    public ResponseEntity<List<ResponseOrderDetailsDTO>> getAllOrderDetails() {
        return super.getAll();
    }

    // GET by ID composto
    @GetMapping("/{ordineId}/{prodottoId}")
    public ResponseEntity<ResponseOrderDetailsDTO> getOrderDetailsById(
            @PathVariable UUID ordineId,
            @PathVariable UUID prodottoId) {

        OrdineProdottoId id = new OrdineProdottoId(ordineId, prodottoId);
        return super.getById(id);
    }

    // POST
    @PostMapping
    public ResponseEntity<ResponseOrderDetailsDTO> createOrderDetails(@RequestBody CreateOrderDetailsDTO createDTO) {
        return super.create(createDTO);
    }

    // PUT by ID composto
    @PutMapping("/{ordineId}/{prodottoId}")
    public ResponseEntity<ResponseOrderDetailsDTO> updateOrderDetails(
            @PathVariable UUID ordineId,
            @PathVariable UUID prodottoId,
            @RequestBody UpdateOrderDetailsDTO updateDTO) {

        OrdineProdottoId id = new OrdineProdottoId(ordineId, prodottoId);
        return super.update(id, updateDTO);
    }

    // DELETE by ID composto
    @DeleteMapping("/{noleggioId}/{prodottoId}")
    public ResponseEntity<Void> deleteOrderDetails(
            @PathVariable UUID noleggioId,
            @PathVariable UUID prodottoId) {

        OrdineProdottoId id = new OrdineProdottoId(noleggioId, prodottoId);
        return super.delete(id);
    }

    // implementazione del metodo astratto getId
    @Override
    protected OrdineProdottoId getId(DettagliOrdine entity) {
        return entity.getId();
    }

}
