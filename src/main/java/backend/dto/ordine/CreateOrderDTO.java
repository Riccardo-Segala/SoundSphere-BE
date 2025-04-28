package backend.dto.ordine;

import backend.model.enums.StatoOrdine;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link backend.model.Ordine}
 */
@Value
public class CreateOrderDTO implements Serializable {
    LocalDate dataAcquisto;
    LocalDate dataConsegna;
    boolean spedizioneGratuita;
    double totale;
    StatoOrdine stato;
}