package backend.controller.admin;

import backend.dto.ordine.ResponseOrderDTO;
import backend.service.OrdineService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path="/api/admin/ordini", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminOrdineController {

    private final OrdineService ordineService;

    public AdminOrdineController(OrdineService ordineService) {
        this.ordineService = ordineService;
    }


     // ADMIN: Recupera l'elenco completo di tutti gli ordini di tutti gli utenti nel sistema
    @GetMapping
    public ResponseEntity<List<ResponseOrderDTO>> getAllOrdersForAdmin() {
        List<ResponseOrderDTO> tuttiGliOrdini = ordineService.findAllOrdersForAdmin();
        return ResponseEntity.ok(tuttiGliOrdini);
    }

}
