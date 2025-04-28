package backend.dto.noleggio;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link backend.model.Noleggio}
 */
public record CreateRentalDTO (
        LocalDate dataInizio,
        LocalDate dataScadenza,
        LocalDate dataRestituzione,
        LocalDate dataPagamento,
        double totale
)implements Serializable {
}