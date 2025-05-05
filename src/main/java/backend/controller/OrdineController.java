package backend.controller;

import backend.dto.ordine.CreateOrderDTO;
import backend.dto.ordine.ResponseOrderDTO;
import backend.dto.ordine.UpdateOrderDTO;
import backend.mapper.OrderMapper;
import backend.model.Ordine;
import backend.service.OrdineService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/ordini")
class OrdineController extends GenericController<Ordine, UUID, CreateOrderDTO, UpdateOrderDTO, ResponseOrderDTO> {
    public OrdineController(OrdineService service, OrderMapper mapper) {
        super(service, mapper);
    }

}
