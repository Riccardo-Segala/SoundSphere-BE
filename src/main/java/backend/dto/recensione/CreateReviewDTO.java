package backend.dto.recensione;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

/**
 * DTO for {@link backend.model.Recensione}
 */
public record CreateReviewDTO(
        LocalDate data,
        int numStelle,
        String descrizione,
        UUID prodottoId,
        UUID utenteId
)implements Serializable {
}