package backend.dto.metodo_pagamento;

import backend.model.enums.TipoPagamento;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

/**
 * DTO for {@link backend.model.MetodoPagamento}
 */
public record ResponsePaymentMethodDTO (
        UUID id,
        String nomeSuCarta,
        String numero,
        LocalDate dataScadenza,
        String paypalEmail,
        TipoPagamento tipoPagamento,
        boolean main
)implements Serializable {
}