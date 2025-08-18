package backend.controller;
import backend.dto.stock.CreateStockDTO;
import backend.dto.stock.ResponseStockDTO;
import backend.dto.stock.UpdateStockDTO;
import backend.mapper.StockMapper;
import backend.model.Stock;
import backend.model.embeddable.FilialeProdottoId;
import backend.service.StockService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path="/api/stock", produces = MediaType.APPLICATION_JSON_VALUE)
public class StockController extends GenericController<Stock, FilialeProdottoId, CreateStockDTO, UpdateStockDTO, ResponseStockDTO> {
    public StockController(StockService service, StockMapper mapper) {
        super(service, mapper);
    }

    @GetMapping
    public ResponseEntity<List<ResponseStockDTO>> getAllStock() {
        return super.getAll();
    }

    // GET by ID composto
    @GetMapping("/{filialeId}/{prodottoId}")
    public ResponseEntity<ResponseStockDTO> getStockItem(
            @PathVariable UUID filialeId,
            @PathVariable UUID prodottoId) {

        FilialeProdottoId id = new FilialeProdottoId(filialeId, prodottoId);
        return super.getById(id);
    }

    // POST
    @PostMapping
    public ResponseEntity<ResponseStockDTO> createStockItem(@RequestBody CreateStockDTO createDTO) {
        return super.create(createDTO);
    }

    // PUT by ID composto
    @PutMapping("/{filialeId}/{prodottoId}")
    public ResponseEntity<ResponseStockDTO> updateStockItem(
            @PathVariable UUID filialeId,
            @PathVariable UUID prodottoId,
            @RequestBody UpdateStockDTO updateDTO) {

        FilialeProdottoId id = new FilialeProdottoId(filialeId, prodottoId);
        return super.update(id, updateDTO);
    }

    // DELETE by ID composto
    @DeleteMapping("/{filialeId}/{prodottoId}")
    public ResponseEntity<Void> deleteStockItem(
            @PathVariable UUID filialeId,
            @PathVariable UUID prodottoId) {

        FilialeProdottoId id = new FilialeProdottoId(filialeId, prodottoId);
        return super.delete(id);
    }

    // implementazione del metodo astratto getId
    @Override
    protected FilialeProdottoId getId(Stock entity) {
        return entity.getId();
    }
}