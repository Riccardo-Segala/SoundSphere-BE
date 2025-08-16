package backend.controller;

import backend.dto.ordine.CreateOrderDTO;
import backend.dto.ordine.ResponseOrderDTO;
import backend.dto.ordine.UpdateOrderDTO;
import backend.mapper.OrderMapper;
import backend.model.Ordine;
import backend.service.OrdineService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/ordini")
class OrdineController extends GenericController<Ordine, UUID, CreateOrderDTO, UpdateOrderDTO, ResponseOrderDTO> {
    public OrdineController(OrdineService service, OrderMapper mapper) {
        super(service, mapper);
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

}
