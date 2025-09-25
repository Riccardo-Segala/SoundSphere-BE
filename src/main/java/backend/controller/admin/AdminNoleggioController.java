package backend.controller.admin;

import backend.dto.noleggio.ResponseRentalDTO;
import backend.service.NoleggioService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path="/api/admin/noleggi", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminNoleggioController {

    private final NoleggioService noleggioService;

    public AdminNoleggioController(NoleggioService noleggioService) {
        this.noleggioService = noleggioService;
    }


    // ADMIN: Recupera l'elenco completo di tutti i noleggi di tutti gli organizzatori nel sistema
    @GetMapping
    public ResponseEntity<List<ResponseRentalDTO>> getAllRentalsForAdmin() {
        List<ResponseRentalDTO> tuttiINoleggi = noleggioService.findAllRentalsForAdmin();
        return ResponseEntity.ok(tuttiINoleggi);
    }

}
