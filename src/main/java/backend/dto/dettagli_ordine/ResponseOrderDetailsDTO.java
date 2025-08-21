package backend.dto.dettagli_ordine;

import backend.dto.prodotto.ResponseProductDTO;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link backend.model.DettagliOrdine}
 */
public record ResponseOrderDetailsDTO (
        UUID ordineId,
        ResponseProductDTO prodotto,
        int quantita
) implements Serializable {
}