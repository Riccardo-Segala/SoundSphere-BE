package backend.dto.dati_statici;

import jakarta.validation.constraints.PositiveOrZero;

import java.io.Serializable;

/**
 * DTO for {@link backend.model.DatiStatici}
 */
public record CreateOrUpdateStaticDataDTO(String nome, @PositiveOrZero double valore) implements Serializable {
}