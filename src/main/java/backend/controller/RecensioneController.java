package backend.controller;

import backend.dto.recensione.CreateReviewDTO;
import backend.dto.recensione.ResponseReviewDTO;
import backend.dto.recensione.UpdateReviewDTO;
import backend.mapper.ReviewMapper;
import backend.model.Recensione;
import backend.service.RecensioneService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/recensioni")
class RecensioneController extends GenericController<Recensione, UUID, CreateReviewDTO, UpdateReviewDTO, ResponseReviewDTO> {
    public RecensioneController(RecensioneService service, ReviewMapper mapper) {
        super(service, mapper);
    }

}
