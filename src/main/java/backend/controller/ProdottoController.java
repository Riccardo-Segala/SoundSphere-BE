package backend.controller;

import backend.dto.prodotto.CatalogProductDTO;
import backend.dto.prodotto.CreateProductDTO;
import backend.dto.prodotto.ResponseProductDTO;
import backend.dto.prodotto.UpdateProductDTO;
import backend.mapper.ProductMapper;
import backend.model.Prodotto;
import backend.service.ProdottoService;
import backend.service.StockService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path="/api/prodotti", produces = MediaType.APPLICATION_JSON_VALUE)
class ProdottoController extends GenericController<Prodotto, UUID, CreateProductDTO, UpdateProductDTO, ResponseProductDTO> {

    final ProdottoService prodottoService;
    private final StockService stockService;
    public ProdottoController(ProdottoService service, ProductMapper mapper, StockService stockService) {
        super(service, mapper);
        this.prodottoService = service;
        this.stockService = stockService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseProductDTO> getProductById(@PathVariable UUID id) {
        return super.getById(id);
    }

    @GetMapping("/{productId}/quantita")
    public ResponseEntity<Integer> getOnlineProductStockQuantityByProductId(@PathVariable UUID productId) {
        // 1. Chiama il servizio per ottenere il dato
        int quantity = stockService.getOnlineStockProductQuantity(productId);

        // 2. Restituisce il numero al frontend
        return ResponseEntity.ok(quantity);
    }

    @GetMapping("/{branchId}/available-products")
    public ResponseEntity<List<ResponseProductDTO>> getProductInStockByBranchId(@PathVariable UUID branchId) {
        List<ResponseProductDTO> prodotti = prodottoService.getProductInStockByBranchId(branchId);
        return ResponseEntity.ok(prodotti);
    }

    @GetMapping("/{productId}/average-stars")
    public ResponseEntity<Double> getAverageStars(@PathVariable UUID productId) {
        Double averageStars = prodottoService.getAverageStars(productId);
        return ResponseEntity.ok(averageStars);
    }
      
    @GetMapping("/marche/online")
    public ResponseEntity<List<String>> getBrandsAvailableOnline() {
        List<String> marcheDisponibili = prodottoService.getAvailableBrandsOnline();
        return ResponseEntity.ok(marcheDisponibili);
    }

    @GetMapping("/search/{categoryId}")
    public ResponseEntity<List<ResponseProductDTO>> getProductsByCategoryId(
            @PathVariable(name = "categoryId") UUID categoryId) {
        List<ResponseProductDTO> prodotti = prodottoService.findProductsByCategoryId(categoryId);
        return ResponseEntity.ok(prodotti);
    }

    @GetMapping("/catalog/online")
    public ResponseEntity<List<CatalogProductDTO>> getOnlineCatalog() {
        List<CatalogProductDTO> catalog = prodottoService.getOnlineProductCatalog();
        return ResponseEntity.ok(catalog);
    }

    @Override
    protected UUID getId(Prodotto entity) {
        return null;
    }
}
