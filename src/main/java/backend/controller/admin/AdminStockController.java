package backend.controller.admin;

import backend.dto.stock.ResponseStockDTO;
import backend.dto.stock.admin.UpdateStockFromAdminDTO;
import backend.model.embeddable.FilialeProdottoId;
import backend.service.StockService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path="/api/admin/stock", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminStockController {

    private final StockService stockService;

    public AdminStockController(StockService stockService) {

        this.stockService = stockService;
    }

    @GetMapping
    public ResponseEntity<List<ResponseStockDTO>> getAllStock() {
        return ResponseEntity.ok(stockService.getAllStock());
    }

    @GetMapping("/{filialeId}/{prodottoId}")
    public ResponseEntity<ResponseStockDTO> getStockById(@PathVariable UUID filialeId,
                                                         @PathVariable UUID prodottoId) {
        FilialeProdottoId id = new FilialeProdottoId(filialeId, prodottoId);
        return ResponseEntity.ok(stockService.getStockById(id));
    }

    @GetMapping("/by-branch/{filialeId}")
    public ResponseEntity<List<ResponseStockDTO>> getStockByBranchId(@PathVariable UUID filialeId) {

        return ResponseEntity.ok(stockService.getAllStockByBranch(filialeId));
    }

    @GetMapping("/by-product/{prodottoId}")
    public ResponseEntity<List<ResponseStockDTO>> getStockByProductId(@PathVariable UUID prodottoId) {

        return ResponseEntity.ok(stockService.getAllStockByProduct(prodottoId));
    }


    @PutMapping("/{filialeId}/{prodottoId}")
    public ResponseEntity<ResponseStockDTO> updateStock(@PathVariable UUID filialeId,
                                                        @PathVariable UUID prodottoId,
                                                        @Valid @RequestBody UpdateStockFromAdminDTO updateAdminDTO) {
        FilialeProdottoId id = new FilialeProdottoId(filialeId, prodottoId);
        return ResponseEntity.ok(stockService.updateAdminStock(id, updateAdminDTO));
    }

}