package backend.service;

import backend.dto.recensione.CreateReviewDTO;
import backend.dto.recensione.ResponseReviewDTO;
import backend.dto.recensione.UpdateReviewDTO;
import backend.mapper.ReviewMapper;
import backend.model.Prodotto;
import backend.model.Recensione;
import backend.model.Utente;
import backend.repository.RecensioneRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class RecensioneService extends GenericService<Recensione, UUID> {
    final RecensioneRepository repository;
    private final ReviewMapper reviewMapper;
    private final ProdottoService prodottoService;
    private final UtenteService utenteService;

    public RecensioneService(RecensioneRepository repository, ReviewMapper reviewMapper, ProdottoService prodottoService, UtenteService utenteService) {
        super(repository);
        this.repository = repository;
        this.reviewMapper = reviewMapper;
        this.prodottoService = prodottoService;
        this.utenteService = utenteService;
    }

    public List<ResponseReviewDTO> getAllByProductId(UUID productId) {
        return (repository).findByProdottoId(productId).stream().map(reviewMapper::toResponseDto).toList();
    }

    @Transactional
    public ResponseReviewDTO createReview(CreateReviewDTO dto, UUID utenteId) {

        Prodotto product = prodottoService.findById(dto.prodottoId())
                .orElseThrow(() -> new EntityNotFoundException("Prodotto non trovato"));

        Utente user = utenteService.findById(utenteId)
                .orElseThrow(() -> new EntityNotFoundException("Utente non trovato"));

        Recensione newReview = reviewMapper.toEntity(dto, product, user);

        Recensione savedReview = repository.save(newReview);
        return reviewMapper.toResponseDto(savedReview);
    }

    @Transactional
    public ResponseReviewDTO updateReview(UpdateReviewDTO dto, UUID userId, UUID reviewId) {
        // 1. Trova l'entità che si vuole aggiornare nel database.
        Recensione reviewToUpdate = repository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("Recensione non trovata con ID: " + reviewId));

        // 2. CONTROLLO DI SICUREZZA
        //    Verifica che l'utente che fa la richiesta sia il proprietario della recensione.
        if (!reviewToUpdate.getUtente().getId().equals(userId)) {
            throw new AccessDeniedException("Non hai il permesso di modificare questa recensione.");
        }

        // 3. Usa il mapper per applicare le modifiche dal DTO all'entità esistente.
        reviewMapper.partialUpdateFromUpdate(dto, reviewToUpdate);

        // 4. Salva l'entità aggiornata nel database.
        Recensione savedReview = repository.save(reviewToUpdate);

        // 5. Restituisci il DTO con i dati aggiornati.
        return reviewMapper.toResponseDto(savedReview);

    }

    @Transactional
    public void deleteReview(UUID reviewId, UUID userId) {
        Recensione reviewToDelete = repository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("Recensione non trovata con ID: " + reviewId));

        // Controllo di sicurezza: solo il proprietario può eliminare la recensione
        if (!reviewToDelete.getUtente().getId().equals(userId)) {
            throw new AccessDeniedException("Non hai il permesso di eliminare questa recensione.");
        }

        repository.delete(reviewToDelete);
    }

    public ResponseReviewDTO getReviewById(UUID reviewId) {
        Recensione review = repository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("Recensione non trovata con ID: " + reviewId));
        return reviewMapper.toResponseDto(review);
    }
}
