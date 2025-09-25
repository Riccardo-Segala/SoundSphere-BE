package backend.dto.checkout;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

public record CheckoutInputRentalDTO(
        UUID metodoPagamentoId,
        UUID indirizzoSpedizioneId,
        LocalDate dataInizio,
        LocalDate dataFine
) implements Serializable {}