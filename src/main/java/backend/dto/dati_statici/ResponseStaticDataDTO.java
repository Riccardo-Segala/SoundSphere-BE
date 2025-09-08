package backend.dto.dati_statici;

import jakarta.validation.constraints.PositiveOrZero;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link backend.model.DatiStatici}
 */
public record ResponseStaticDataDTO(UUID id, String nome, @PositiveOrZero double valore) implements Serializable {
}