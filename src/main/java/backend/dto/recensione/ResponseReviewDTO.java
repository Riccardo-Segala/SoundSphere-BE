package backend.dto.recensione;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

/**
 * DTO for {@link backend.model.Recensione}
 */
public record ResponseReviewDTO (
        UUID id,
        LocalDate data,
        int numStelle,
        String descrizione,
        String prodottoNome,
        String utenteNome,
        String utenteCognome
)implements Serializable {
}