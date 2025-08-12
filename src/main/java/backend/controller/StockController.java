package backend.controller;

import backend.dto.stock.CreateStockDTO;
import backend.dto.stock.ResponseStockDTO;
import backend.dto.stock.UpdateStockDTO;
import backend.mapper.StockMapper;
import backend.model.Stock;
import backend.model.embeddable.FilialeProdottoId;
import backend.service.StockService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/stock")
@PreAuthorize("hasAuthority('GESTIONE_STOCK')")
public class StockController extends GenericController<Stock, FilialeProdottoId, CreateStockDTO, UpdateStockDTO, ResponseStockDTO> {
    public StockController(StockService service, StockMapper mapper) {
        super(service, mapper);
    }
}