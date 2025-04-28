package backend.dto.stock;

import lombok.Value;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link backend.model.Stock}
 */
@Value
public class UpdateStockDTO implements Serializable {
    UUID filialeId;
    UUID prodottoId;
    int quantita;
    int quantitaPerNoleggio;
}