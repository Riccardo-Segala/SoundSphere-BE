package backend.controller;

import backend.dto.carrello.CreateCartDTO;
import backend.dto.carrello.ResponseCartDTO;
import backend.dto.carrello.UpdateCartDTO;
import backend.mapper.CartMapper;
import backend.model.Carrello;
import backend.model.embeddable.UtenteProdottoId;
import backend.service.CarrelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/carrello")
class CarrelloController extends GenericController <Carrello, UtenteProdottoId, CreateCartDTO, UpdateCartDTO, ResponseCartDTO> {
    public CarrelloController(CarrelloService service, CartMapper mapper) {
        super(service, mapper);
    }

    @Autowired
    private CarrelloService carrelloService;

    @GetMapping("/totale-parziale/{id}")
    public double getTotaleParziale(@PathVariable UUID id) {
        return carrelloService.calcolaTotaleParziale(id);
    }

    @GetMapping("/totale-finale/{id}")
    public double getTotaleFinale(@PathVariable UUID id) {
        return carrelloService.calcolaTotaleFinale(id);
    }

}
