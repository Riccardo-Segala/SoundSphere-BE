package backend.dto.carrello;

import backend.model.Carrello;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link Carrello}
 */
//@Value
public record UpdateCartItemDTO(
        UUID prodottoId,
        int quantita,
        boolean wishlist
) implements Serializable {
}