package backend.dto.checkout;

import java.io.Serializable;
import java.util.UUID;

public record ProductOrderOutputDTO(
        UUID prodottoId,
        String nomeProdotto,
        int quantita
)implements Serializable {}
