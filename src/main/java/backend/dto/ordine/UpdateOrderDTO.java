package backend.dto.ordine;

import backend.model.enums.StatoOrdine;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

/**
 * DTO for {@link backend.model.Ordine}
 */
public record UpdateOrderDTO (
        UUID id,
        LocalDate dataAcquisto,
        LocalDate dataConsegna,
        boolean spedizioneGratuita,
        double totale,
        StatoOrdine stato,
        UUID indirizzoId
) implements Serializable {
}