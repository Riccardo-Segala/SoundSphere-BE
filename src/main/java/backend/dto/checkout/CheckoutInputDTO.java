package backend.dto.checkout;

import java.util.List;
import java.util.UUID;

public record CheckoutInputDTO(
    UUID metodoPagamentoId,
    UUID indirizzoSpedizioneId
)
{}
