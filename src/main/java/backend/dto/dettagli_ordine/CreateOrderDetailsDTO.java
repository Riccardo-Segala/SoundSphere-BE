package backend.dto.dettagli_ordine;

import backend.dto.prodotto.ResponseProductDTO;
import lombok.Value;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link backend.model.DettagliOrdine}
 */
@Value
public class CreateOrderDetailsDTO implements Serializable {
    UUID ordineId;
    ResponseProductDTO prodotto;
    UUID utenteId;
    int quantita;
}