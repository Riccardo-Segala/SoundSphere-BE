package backend.controller;

import backend.dto.prodotto.CreateProductDTO;
import backend.dto.prodotto.ResponseProductDTO;
import backend.dto.prodotto.UpdateProductDTO;
import backend.mapper.ProductMapper;
import backend.model.Prodotto;
import backend.service.ProdottoService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/prodotti")
class ProdottoController extends GenericController<Prodotto, UUID, CreateProductDTO, UpdateProductDTO, ResponseProductDTO> {
    public ProdottoController(ProdottoService service, ProductMapper mapper) {
        super(service, mapper);
    }
}
