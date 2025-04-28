package backend.dto.stock;

import backend.dto.prodotto.ResponseProductDTO;
import lombok.Value;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link backend.model.Stock}
 */
@Value
public class ResponseStockDTO implements Serializable {
    UUID filialeId;
    String filialeNome;
    ResponseProductDTO prodotto;
    int quantita;
    int quantitaPerNoleggio;
}