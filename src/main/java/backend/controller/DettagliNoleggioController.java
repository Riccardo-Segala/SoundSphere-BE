package backend.controller;

import backend.dto.dettagli_noleggio.CreateRentalDetailsDTO;
import backend.dto.dettagli_noleggio.ResponseRentalDetailsDTO;
import backend.dto.dettagli_noleggio.UpdateRentalDetailsDTO;
import backend.mapper.RentalDetailsMapper;
import backend.model.DettagliNoleggio;
import backend.service.DettagliNoleggioService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/dettagli-noleggi")
class DettagliNoleggioController extends GenericController<DettagliNoleggio, UUID, CreateRentalDetailsDTO, UpdateRentalDetailsDTO, ResponseRentalDetailsDTO> {
    public DettagliNoleggioController(DettagliNoleggioService service, RentalDetailsMapper mapper) {
        super(service, mapper);
    }

}
