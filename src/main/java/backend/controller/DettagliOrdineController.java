package backend.controller;

import backend.dto.dettagli_ordine.CreateOrderDetailsDTO;
import backend.dto.dettagli_ordine.ResponseOrderDetailsDTO;
import backend.dto.dettagli_ordine.UpdateOrderDetailsDTO;
import backend.mapper.OrderDetailsMapper;
import backend.model.DettagliOrdine;
import backend.service.DettagliOrdineService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/dettagli-ordini")
class DettagliOrdineController extends GenericController<DettagliOrdine, UUID, CreateOrderDetailsDTO, UpdateOrderDetailsDTO, ResponseOrderDetailsDTO> {
    public DettagliOrdineController(DettagliOrdineService service, OrderDetailsMapper mapper) {
        super(service, mapper);
    }

}
