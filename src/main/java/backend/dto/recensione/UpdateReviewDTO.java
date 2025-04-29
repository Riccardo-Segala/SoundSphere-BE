package backend.dto.recensione;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

/**
 * DTO for {@link backend.model.Recensione}
 */
public record UpdateReviewDTO(
        UUID id,
        LocalDate data,
        int numStelle,
        String descrizione
) implements Serializable {
}