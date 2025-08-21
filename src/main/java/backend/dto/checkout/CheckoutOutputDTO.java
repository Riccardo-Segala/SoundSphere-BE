package backend.dto.checkout;

import backend.dto.indirizzo_utente.ResponseUserAddressDTO;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record CheckoutOutputDTO(
        UUID ordineId,
        String numeroOrdine, // Un numero più leggibile per il cliente, es. "2025-000123"
        LocalDate dataOrdine,
        String statoOrdine, // Es. "IN_ELABORAZIONE", "CONFERMATO"
        BigDecimal importoTotale,
        ResponseUserAddressDTO indirizzoSpedizione,
        LocalDate dataConsegnaStimata,
        List<ProductOrderOutputDTO> prodottiOrdinati
)
{}
