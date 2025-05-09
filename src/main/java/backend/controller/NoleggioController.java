package backend.controller;

import backend.dto.noleggio.CreateRentalDTO;
import backend.dto.noleggio.ResponseRentalDTO;
import backend.dto.noleggio.UpdateRentalDTO;
import backend.mapper.RentalMapper;
import backend.model.Noleggio;
import backend.service.NoleggioService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/noleggi")
class NoleggioController extends GenericController<Noleggio, UUID, CreateRentalDTO, UpdateRentalDTO, ResponseRentalDTO> {
    public NoleggioController(NoleggioService service, RentalMapper mapper) {
        super(service, mapper);
    }

}
