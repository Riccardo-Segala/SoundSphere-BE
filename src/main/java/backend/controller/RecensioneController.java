package backend.controller;

import backend.dto.recensione.CreateReviewDTO;
import backend.dto.recensione.ResponseReviewDTO;
import backend.dto.recensione.UpdateReviewDTO;
import backend.mapper.ReviewMapper;
import backend.model.Recensione;
import backend.security.CustomUserDetails;
import backend.service.RecensioneService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path="/api/recensioni", produces = MediaType.APPLICATION_JSON_VALUE)
class RecensioneController extends GenericController<Recensione, UUID, CreateReviewDTO, UpdateReviewDTO, ResponseReviewDTO> {
    private final RecensioneService service;
    public RecensioneController(RecensioneService service, ReviewMapper mapper) {
        super(service, mapper);

        this.service = service;
    }


    @GetMapping("/{productId}/all")
    public ResponseEntity<List<ResponseReviewDTO>> getAllReviewsByProductId(@PathVariable UUID productId) {
        List<ResponseReviewDTO> reviews = service.getAllByProductId(productId);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<ResponseReviewDTO> getReviewById(@PathVariable UUID reviewId) {
        ResponseReviewDTO review = service.getReviewById(reviewId);
        return ResponseEntity.ok(review);
    }

    @PostMapping
    public ResponseEntity<ResponseReviewDTO> createReview(
            @RequestBody CreateReviewDTO createDTO,
            @AuthenticationPrincipal CustomUserDetails userDetails)
    {
        UUID utenteId = userDetails.getId();
        ResponseReviewDTO cretedReview = service.createReview(createDTO, utenteId);
        return new ResponseEntity<>(cretedReview, HttpStatus.CREATED);
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<ResponseReviewDTO> updateReview(@RequestBody UpdateReviewDTO updateDTO,
                                                          @PathVariable UUID reviewId,
                                                          @AuthenticationPrincipal CustomUserDetails userDetails)
    {
        UUID userId = userDetails.getId();
        ResponseReviewDTO updatedReview = service.updateReview(updateDTO, userId, reviewId);
        return new ResponseEntity<>(updatedReview, HttpStatus.OK);
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable UUID reviewId,
                                             @AuthenticationPrincipal CustomUserDetails userDetails)
    {
        UUID userId = userDetails.getId();
        service.deleteReview(reviewId, userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    protected UUID getId(Recensione entity) {
        return entity.getId();
    }

}
