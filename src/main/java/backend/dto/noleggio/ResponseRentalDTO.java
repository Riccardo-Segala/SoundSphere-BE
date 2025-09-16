package backend.dto.noleggio;

import backend.dto.dettagli_noleggio.ResponseRentalDetailsDTO;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * DTO for {@link backend.model.Noleggio}
 */
public record ResponseRentalDTO (
        UUID id,
        LocalDate dataInizio,
        LocalDate dataScadenza,
        LocalDate dataRestituzione,
        LocalDate dataPagamento,
        double totale,
        List<ResponseRentalDetailsDTO> dettagli
) implements Serializable {
}