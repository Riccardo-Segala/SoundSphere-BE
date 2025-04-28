package backend.dto.dettagli_noleggio;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link backend.model.DettagliNoleggio}
 */

public record ResponseRentalDetailsDTO (
        UUID noleggioId,
        UUID prodottoId,
        String prodottoNome,
        String prodottoDescrizione,
        double prodottoCostoGiornaliero,
        String prodottoPathImmagine,
        UUID organizzatoreEventiId,
        int quantita
)implements Serializable {
}