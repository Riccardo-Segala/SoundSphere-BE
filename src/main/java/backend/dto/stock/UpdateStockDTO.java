package backend.dto.stock;

import backend.dto.prodotto.ResponseProductDTO;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link backend.model.Stock}
 */
public record UpdateStockDTO (
        UUID filialeId,
        String filialeNome,
        ResponseProductDTO prodotto,
        int quantita,
        int quantitaPerNoleggio
) implements Serializable {
}