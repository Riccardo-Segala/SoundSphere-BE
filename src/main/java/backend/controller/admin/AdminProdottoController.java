package backend.controller.admin;

import backend.dto.prodotto.CreateProductDTO;
import backend.dto.prodotto.ResponseProductDTO;
import backend.dto.prodotto.UpdateProductDTO;
import backend.service.admin.AdminProdottoService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path="/api/admin/prodotti", produces = MediaType.APPLICATION_JSON_VALUE)
class AdminProdottoController {
    private final AdminProdottoService adminProdottoService;

    public AdminProdottoController(AdminProdottoService service) {
        this.adminProdottoService = service;

    }

    @GetMapping
    public ResponseEntity<List<ResponseProductDTO>> getAllProducts() {
        return ResponseEntity.ok(adminProdottoService.getAll());
    }

    @PostMapping
    public ResponseEntity<ResponseProductDTO> createProduct(@RequestBody CreateProductDTO createDTO) {
        return ResponseEntity.ok(adminProdottoService.createProduct(createDTO));
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ResponseProductDTO> updateProduct(@PathVariable UUID productId, @RequestBody UpdateProductDTO updateDTO) {
        return ResponseEntity.ok(adminProdottoService.updateProduct(productId, updateDTO));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID productId) {
        return ResponseEntity.ok(adminProdottoService.deleteProduct(productId));
    }

}
