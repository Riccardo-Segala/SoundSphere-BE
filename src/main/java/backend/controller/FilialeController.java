package backend.controller;

import backend.dto.filiale.CreateBranchDTO;
import backend.dto.filiale.ResponseBranchDTO;
import backend.dto.filiale.UpdateBranchDTO;
import backend.mapper.BranchMapper;
import backend.model.Filiale;
import backend.model.Prodotto;
import backend.service.FilialeService;
import backend.service.StockService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path="/api/filiale", produces = MediaType.APPLICATION_JSON_VALUE)
class FilialeController extends GenericController <Filiale, UUID, CreateBranchDTO, UpdateBranchDTO, ResponseBranchDTO> {
    private final StockService stockService;

    FilialeController(FilialeService service, BranchMapper mapper, StockService stockService) {
        super(service, mapper);
        this.stockService = stockService;
    }

    @GetMapping("/{id}/stock")
    public ResponseEntity<List<Prodotto>> getProductInStockByBranchId(@PathVariable UUID id) {
        List<Prodotto> prodotti = stockService.getProductInStockByBranchId(id);
        return ResponseEntity.ok(prodotti);
    }

    @GetMapping
    public ResponseEntity<List<ResponseBranchDTO>> getAllBranches() {
        return super.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseBranchDTO> getBranchById(@PathVariable UUID id) {
        return super.getById(id);
    }

    @PostMapping
    public ResponseEntity<ResponseBranchDTO> createBranch(@RequestBody CreateBranchDTO createDTO) {
        return super.create(createDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseBranchDTO> updateBranch(@PathVariable UUID id, @RequestBody UpdateBranchDTO updateDTO) {
        return super.update(id, updateDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBranch(@PathVariable UUID id) {
        return super.delete(id);
    }

    @Override
    protected UUID getId(Filiale entity) {
        return entity.getId();
    }
}
