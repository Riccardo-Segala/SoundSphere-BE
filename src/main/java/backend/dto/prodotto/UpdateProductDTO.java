package backend.dto.prodotto;


import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link backend.model.Prodotto}
 */

public record UpdateProductDTO(
        UUID id,
        String nome,
        String descrizione,
        double prezzo,
        boolean isRentable,
        double costoGiornaliero,
        String pathImmagine
) implements Serializable {
}