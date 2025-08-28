package backend.dto.prodotto;


import java.io.Serializable;

/**
 * DTO for {@link backend.model.Prodotto}
 */

public record UpdateProductDTO(
        String nome,
        String descrizione,
        double prezzo,
        boolean rentable,
        double costoGiornaliero,
        String pathImmagine
) implements Serializable {
}