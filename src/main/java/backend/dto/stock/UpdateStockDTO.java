package backend.dto.stock;

import jakarta.validation.constraints.Min;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link backend.model.Stock}
 */
public record UpdateStockDTO (
        UUID prodottoId,
        @Min(value = 0, message = "La quantità non può essere negativa.")
        Integer quantita
) implements Serializable {
}