package backend.dto.prodotto;

import java.io.Serializable;

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
        String pathImmagine
) implements Serializable {
}