package backend.dto.carrello;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link backend.model.Carrello}
 */
public record UpdateCartDTO(
        UUID utenteId,
        UUID prodottoId,
        int quantita,
        boolean wishlist
) implements Serializable {
}