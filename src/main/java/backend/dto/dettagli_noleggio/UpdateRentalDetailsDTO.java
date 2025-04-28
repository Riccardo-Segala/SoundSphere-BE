package backend.dto.dettagli_noleggio;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link backend.model.DettagliNoleggio}
 */

public record UpdateRentalDetailsDTO (
        UUID noleggioId,
        UUID prodottoId,
        UUID organizzatoreEventiId,
        int quantita
)implements Serializable {
}