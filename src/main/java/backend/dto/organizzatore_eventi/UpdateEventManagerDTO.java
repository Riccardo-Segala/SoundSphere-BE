package backend.dto.organizzatore_eventi;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

/**
 * DTO for {@link backend.model.OrganizzatoreEventi}
 */
public record UpdateEventManagerDTO (
        UUID id,
        LocalDate dataScadenza
)implements Serializable {
}