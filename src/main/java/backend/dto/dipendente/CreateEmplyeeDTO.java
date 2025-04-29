package backend.dto.dipendente;

import lombok.Value;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

/**
 * DTO for {@link backend.model.Dipendente}
 */
public record CreateEmplyeeDTO (
        UUID id,
        double stipendio,
        LocalDate scadenzaContratto,
        LocalDate dataAssunzione,
        UUID filialeId
) implements Serializable {
}