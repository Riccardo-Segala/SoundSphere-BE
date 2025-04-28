package backend.dto.recensione;

import lombok.Value;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

/**
 * DTO for {@link backend.model.Recensione}
 */
@Value
public class ResponseReviewDTO implements Serializable {
    UUID id;
    LocalDate data;
    int numStelle;
    String descrizione;
    String prodottoNome;
    String utenteNome;
    String utenteCognome;
}