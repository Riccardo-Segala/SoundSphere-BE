package backend.dto.ordine;

import backend.model.enums.StatoOrdine;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link backend.model.Ordine}
 */
public record CreateOrderDTO (
        LocalDate dataAcquisto,
        LocalDate dataConsegna,
        boolean spedizioneGratuita,
        double totale,
        StatoOrdine stato
) implements Serializable {
}