package backend.dto.prodotto;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 * DTO for {@link backend.model.Prodotto}
 */

public record CreateProductDTO(
        String nome,
        String descrizione,
        double prezzo,
        String marca,
        boolean rentable,
        double costoGiornaliero,
        String pathImmagine,
        List<UUID> categorieIds
) implements Serializable {
}