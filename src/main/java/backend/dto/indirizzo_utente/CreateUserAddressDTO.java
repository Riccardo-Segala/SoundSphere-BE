package backend.dto.indirizzo_utente;

import backend.model.enums.TipologiaIndirizzo;
import lombok.Value;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link backend.model.IndirizzoUtente}
 */
@Value
public class CreateUserAddressDTO implements Serializable {
    String via;
    String civico;
    String cap;
    String citta;
    String provincia;
    String nazione;
    boolean isDefault;
    TipologiaIndirizzo tipologia;
    UUID utenteId;
    String utenteNome;
    String utenteCognome;
}