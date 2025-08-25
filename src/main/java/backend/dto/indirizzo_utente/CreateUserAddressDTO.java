package backend.dto.indirizzo_utente;

import backend.model.enums.TipologiaIndirizzo;

import java.io.Serializable;

/**
 * DTO for {@link backend.model.IndirizzoUtente}
 */
public record CreateUserAddressDTO (
        String via,
        String civico,
        String cap,
        String citta,
        String provincia,
        String nazione,
        boolean isDefault,
        TipologiaIndirizzo tipologia
) implements Serializable {
}