package backend.dto.checkout;

import java.util.UUID;

public record ProductOrderInputDTO(
        UUID prodottoId,
        int quantita
)
{}
