package backend.dto.stock.admin;

import jakarta.validation.constraints.Min;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link backend.model.Stock}
 */
public record UpdateStockFromAdminDTO(
        UUID filialeId,
        UUID prodottoId,
        @Min(value = 0, message = "La quantità non può essere negativa.")
        Integer quantita,
        @Min(value = 0, message = "La quantità per noleggio non può essere negativa.")
        Integer quantitaPerNoleggio
) implements Serializable {
}