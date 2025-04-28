package backend.dto.ordine;

import backend.model.enums.StatoOrdine;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

/**
 * DTO for {@link backend.model.Ordine}
 */
@Value
public class ResponseOrderDTO implements Serializable {
    UUID id;
    LocalDate dataAcquisto;
    LocalDate dataConsegna;
    boolean spedizioneGratuita;
    double totale;
    StatoOrdine stato;
}