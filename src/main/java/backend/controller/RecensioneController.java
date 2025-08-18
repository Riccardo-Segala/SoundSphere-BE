package backend.controller;

import backend.dto.recensione.CreateReviewDTO;
import backend.dto.recensione.ResponseReviewDTO;
import backend.dto.recensione.UpdateReviewDTO;
import backend.mapper.ReviewMapper;
import backend.model.Recensione;
import backend.service.RecensioneService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path="/api/recensioni", produces = MediaType.APPLICATION_JSON_VALUE)
class RecensioneController extends GenericController<Recensione, UUID, CreateReviewDTO, UpdateReviewDTO, ResponseReviewDTO> {
    public RecensioneController(RecensioneService service, ReviewMapper mapper) {
        super(service, mapper);
    }

    @GetMapping
    public ResponseEntity<List<ResponseReviewDTO>> getAllReviews() {
        return super.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseReviewDTO> getReviewById(@PathVariable UUID id) {
        return super.getById(id);
    }

    @PostMapping
    public ResponseEntity<ResponseReviewDTO> createReview(@RequestBody CreateReviewDTO createDTO) {
        return super.create(createDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseReviewDTO> updateReview(@PathVariable UUID id, @RequestBody UpdateReviewDTO updateDTO) {
        return super.update(id, updateDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable UUID id) {
        return super.delete(id);
    }

    @Override
    protected UUID getId(Recensione entity) {
        return entity.getId();
    }

}
