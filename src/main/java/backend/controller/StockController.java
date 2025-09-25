package backend.controller;
import backend.dto.stock.CreateStockDTO;
import backend.dto.stock.ResponseStockDTO;
import backend.dto.stock.UpdateStockDTO;
import backend.mapper.StockMapper;
import backend.model.Stock;
import backend.model.embeddable.FilialeProdottoId;
import backend.security.CustomUserDetails;
import backend.service.StockService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path="/api/stock", produces = MediaType.APPLICATION_JSON_VALUE)
public class StockController extends GenericController<Stock, FilialeProdottoId, CreateStockDTO, UpdateStockDTO, ResponseStockDTO> {

    private final StockService stockService;
    public StockController(StockService service, StockMapper mapper, StockService stockService) {
        super(service, mapper);
        this.stockService = stockService;
    }

    @GetMapping
    public ResponseEntity<List<ResponseStockDTO>> getAllStock() {
        return super.getAll();
    }

    @GetMapping("/online")
    public ResponseEntity<List<ResponseStockDTO>> getOnlineStock() {
        // 1. Chiama il servizio per ottenere i dati, già pronti in formato DTO
        List<ResponseStockDTO> avaiableProducts = stockService.getOnlineStock();

        // 2. Restituisce i dati al frontend con uno status HTTP 200 (OK)
        return ResponseEntity.ok(avaiableProducts);
    }

    // POST
    @PostMapping
    public ResponseEntity<ResponseStockDTO> createStockItem(@RequestBody CreateStockDTO createDTO) {
        return super.create(createDTO);
    }

    @Override
    protected FilialeProdottoId getId(Stock entity) {
        return entity.getId();
    }


    // --- ENDPOINT PER IL DIPENDENTE ---

    @GetMapping("/my-filiale")
    @PreAuthorize("hasAuthority('GESTIONE_STOCK')")
    public ResponseEntity<List<ResponseStockDTO>> getMyFilialeStock(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        List<ResponseStockDTO> stock = stockService.getStockForMyFiliale(userDetails.getId());
        return ResponseEntity.ok(stock);
    }

    @GetMapping("/my-filiale/{prodottoId}")
    @PreAuthorize("hasAuthority('GESTIONE_STOCK')")
    public ResponseEntity<ResponseStockDTO> getSpecificProductStock(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable UUID prodottoId) {

        ResponseStockDTO stockDetails = stockService.getStockForSpecificProductInMyFiliale(userDetails.getId(), prodottoId);

        return ResponseEntity.ok(stockDetails);
    }

    @PutMapping("/my-filiale")
    @PreAuthorize("hasAuthority('GESTIONE_STOCK')")
    public ResponseEntity<ResponseStockDTO> updateMyFilialeStock(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody UpdateStockDTO updateDTO) {

        ResponseStockDTO updatedStock = stockService.updateStockForMyFiliale(userDetails.getId(), updateDTO);
        return ResponseEntity.ok(updatedStock);
    }

}