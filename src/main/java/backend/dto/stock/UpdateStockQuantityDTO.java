package backend.dto.stock;

import jakarta.validation.constraints.Min;
import java.io.Serializable;

/**
 * DTO specifico per l'operazione di aggiornamento della quantità di stock
 * da parte di un dipendente. Espone solo il campo necessario
 * e include la validazione per impedire valori negativi.
 */
public record UpdateStockQuantityDTO(
        @Min(value = 0, message = "La quantità non può essere negativa.")
        int quantita
) implements Serializable {
}