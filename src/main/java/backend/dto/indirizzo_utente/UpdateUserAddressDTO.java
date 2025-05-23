package backend.dto.indirizzo_utente;

import backend.model.enums.TipologiaIndirizzo;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link backend.model.IndirizzoUtente}
 */
public record UpdateUserAddressDTO (
        UUID id,
        String via,
        String civico,
        String cap,
        String citta,
        String provincia,
        String nazione,
        boolean isDefault,
        TipologiaIndirizzo tipologia
)implements Serializable {
}