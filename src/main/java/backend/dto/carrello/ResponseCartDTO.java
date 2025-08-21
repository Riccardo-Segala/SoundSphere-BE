package backend.dto.carrello;

import backend.dto.prodotto.ResponseProductDTO;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link backend.model.Carrello}
 */
public record ResponseCartDTO(
        ResponseProductDTO prodotto,
        int quantita,
        boolean wishlist
) implements Serializable {
}