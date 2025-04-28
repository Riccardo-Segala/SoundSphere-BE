package backend.dto.indirizzo_utente;

import backend.model.IndirizzoUtente;
import backend.model.enums.TipologiaIndirizzo;
import lombok.Value;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link IndirizzoUtente}
 */
@Value
public class ResponseUserAddressDTO implements Serializable {
    UUID id;
    String via;
    String civico;
    String cap;
    String citta;
    String provincia;
    String nazione;
    boolean isDefault;
    TipologiaIndirizzo tipologia;
}