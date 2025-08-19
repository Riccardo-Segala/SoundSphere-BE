package backend.dto.checkout;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductOrderOutputDTO(
        UUID prodottoId,
        String nomeProdotto,
        int quantita,
        BigDecimal prezzoUnitarioCongelato
)
{}
