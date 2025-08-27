package backend.controller.admin;

import backend.controller.GenericController;
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
@RequestMapping(path="/api/admin/prodotti", produces = MediaType.APPLICATION_JSON_VALUE)
class AdminProdottoController extends GenericController<Prodotto, UUID, CreateProductDTO, UpdateProductDTO, ResponseProductDTO> {

    public AdminProdottoController(ProdottoService service, ProductMapper mapper) {
        super(service, mapper);
    }

    @GetMapping
    public ResponseEntity<List<ResponseProductDTO>> getAllProducts() {
        return super.getAll();
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
}
