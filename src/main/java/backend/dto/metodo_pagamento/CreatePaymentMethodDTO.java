package backend.dto.metodo_pagamento;

import backend.model.enums.TipoPagamento;

import java.io.Serializable;
import java.time.LocalDate;

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
        boolean main
) implements Serializable {
}