package backend.controller;

import backend.dto.prodotto.CreateProductDTO;
import backend.dto.prodotto.ResponseProductDTO;
import backend.dto.prodotto.UpdateProductDTO;
import backend.mapper.ProductMapper;
import backend.model.Prodotto;
import backend.service.ProdottoService;
import backend.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path="/api/prodotti", produces = MediaType.APPLICATION_JSON_VALUE)
class ProdottoController extends GenericController<Prodotto, UUID, CreateProductDTO, UpdateProductDTO, ResponseProductDTO> {
    private final StockService stockService;

    @Autowired
    public ProdottoController(ProdottoService service, ProductMapper mapper, StockService stockService) {
        super(service, mapper);
        this.stockService = stockService;

    }

    @GetMapping
    public ResponseEntity<List<ResponseProductDTO>> getAllProducts() {
        return super.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseProductDTO> getProductById(@PathVariable UUID id) {
        return super.getById(id);
    }

    @PostMapping
    public ResponseEntity<ResponseProductDTO> createProduct(@RequestBody CreateProductDTO createDTO) {
        return super.create(createDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseProductDTO> updateProduct(@PathVariable UUID id, @RequestBody UpdateProductDTO updateDTO) {
        return super.update(id, updateDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID id) {
        return super.delete(id);
    }

    @Override
    protected UUID getId(Prodotto entity) {
        return entity.getId();
    }

    @GetMapping("/marche/online")
    public ResponseEntity<List<String>> getMarcheDisponibiliOnline() {
        List<String> marcheDisponibili = stockService.getMarcheDisponibiliOnline();
        return ResponseEntity.ok(marcheDisponibili);
    }
}
