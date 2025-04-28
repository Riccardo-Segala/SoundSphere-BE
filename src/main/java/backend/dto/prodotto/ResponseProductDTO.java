package backend.dto.prodotto;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link backend.model.Prodotto}
 */

public record ResponseProductDTO(
        UUID id,
        String nome,
        String descrizione,
        double prezzo,
        String marca,
        String pathImmagine
) implements Serializable {
}