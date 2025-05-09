package backend.dto.stock;



import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link backend.model.Stock}
 */
public record CreateStockDTO (
        UUID filialeId,
        UUID prodottoId,
        int quantita,
        int quantitaPerNoleggio
) implements Serializable {
}