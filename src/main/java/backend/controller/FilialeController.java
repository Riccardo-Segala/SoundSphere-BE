package backend.controller;

import backend.dto.filiale.CreateBranchDTO;
import backend.dto.filiale.ResponseBranchDTO;
import backend.dto.filiale.UpdateBranchDTO;
import backend.mapper.BranchMapper;
import backend.model.Filiale;
import backend.model.Prodotto;
import backend.service.FilialeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/filiale")
class FilialeController extends GenericController <Filiale, UUID, CreateBranchDTO, UpdateBranchDTO, ResponseBranchDTO> {
    public FilialeController(FilialeService service, BranchMapper mapper) {
        super(service, mapper);
    }

    @GetMapping("/{id}/stock")
    public ResponseEntity<List<Prodotto>> getProductInStockByBranchId(@PathVariable UUID id) {
        FilialeService filialeService = (FilialeService) this.getService();
        List<Prodotto> prodotti = filialeService.getProductInStockByBranchId(id);
        return ResponseEntity.ok(prodotti);
    }

}
