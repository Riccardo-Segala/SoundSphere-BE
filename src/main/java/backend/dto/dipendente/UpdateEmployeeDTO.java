package backend.dto.dipendente;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

/**
 * DTO for {@link backend.model.Dipendente}
 */
public record UpdateEmployeeDTO (
        UUID id,
        double stipendio,
        LocalDate scadenzaContratto,
        LocalDate dataAssunzione,
        UUID filialeId
)implements Serializable {
}