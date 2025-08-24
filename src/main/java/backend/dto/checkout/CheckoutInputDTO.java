package backend.dto.checkout;

import java.io.Serializable;
import java.util.UUID;

public record CheckoutInputDTO(
    UUID metodoPagamentoId,
    UUID indirizzoSpedizioneId
)implements Serializable {}
