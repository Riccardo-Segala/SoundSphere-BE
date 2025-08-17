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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    @GetMapping
    public ResponseEntity<List<ResponseCartDTO>> getAllCarts() {
        return super.getAll();
    }

    // GET by ID composto
    @GetMapping("/{utenteId}/{prodottoId}")
    public ResponseEntity<ResponseCartDTO> getCartById(
            @PathVariable UUID utenteId,
            @PathVariable UUID prodottoId) {

        UtenteProdottoId id = new UtenteProdottoId(utenteId, prodottoId);
        return super.getById(id);
    }

    // POST
    @PostMapping
    public ResponseEntity<ResponseCartDTO> createCart(@RequestBody CreateCartDTO createDTO) {
        return super.create(createDTO);
    }

    // PUT by ID composto
    @PutMapping("/{utenteId}/{prodottoId}")
    public ResponseEntity<ResponseCartDTO> updateCart(
            @PathVariable UUID utenteId,
            @PathVariable UUID prodottoId,
            @RequestBody UpdateCartDTO updateDTO) {

        UtenteProdottoId id = new UtenteProdottoId(utenteId, prodottoId);
        return super.update(id, updateDTO);
    }

    // DELETE by ID composto
    @DeleteMapping("/{utenteId}/{prodottoId}")
    public ResponseEntity<Void> deleteCart(
            @PathVariable UUID utenteId,
            @PathVariable UUID prodottoId) {

        UtenteProdottoId id = new UtenteProdottoId(utenteId, prodottoId);
        return super.delete(id);
    }

    // implementazione del metodo astratto getId
    @Override
    protected UtenteProdottoId getId(Carrello entity) {
        return entity.getId();
    }

}
