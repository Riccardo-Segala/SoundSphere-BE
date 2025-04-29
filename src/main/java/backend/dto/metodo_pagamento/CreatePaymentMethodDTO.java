package backend.dto.metodo_pagamento;

import backend.model.enums.TipoPagamento;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

/**
 * DTO for {@link backend.model.MetodoPagamento}
 */
public record CreatePaymentMethodDTO (
        String nomeSuCarta,
        String numero,
        String cvv,
        LocalDate dataScadenza,
        String paypalEmail,
        TipoPagamento tipoPagamento,
        boolean isDefault,
        UUID utenteId
) implements Serializable {
}