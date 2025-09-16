package backend.dto.prodotto;

import backend.dto.categoria.ResponseCategoryDTO;

import java.io.Serializable;
import java.util.List;
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
        boolean rentable,
        double costoGiornaliero,
        String pathImmagine,
        List<ResponseCategoryDTO> categorie
) implements Serializable {
}