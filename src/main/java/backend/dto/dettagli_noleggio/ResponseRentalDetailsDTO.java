package backend.dto.dettagli_noleggio;

import backend.dto.prodotto.ResponseProductDTO;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link backend.model.DettagliNoleggio}
 */

public record ResponseRentalDetailsDTO (
        UUID noleggioId,
        ResponseProductDTO prodotto,
        int quantita
)implements Serializable {
}