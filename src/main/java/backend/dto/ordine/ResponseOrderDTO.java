package backend.dto.ordine;

import backend.dto.dettagli_ordine.ResponseOrderDetailsDTO;
import backend.model.enums.StatoOrdine;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * DTO for {@link backend.model.Ordine}
 */
public record ResponseOrderDTO (
        UUID id,
        LocalDate dataAcquisto,
        LocalDate dataConsegna,
        boolean spedizioneGratuita,
        double totale,
        StatoOrdine stato,
         List<ResponseOrderDetailsDTO> dettagli
) implements Serializable {
}