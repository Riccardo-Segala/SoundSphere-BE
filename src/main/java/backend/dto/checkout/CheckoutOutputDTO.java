package backend.dto.checkout;

import backend.dto.indirizzo_utente.ResponseUserAddressDTO;
import backend.dto.vantaggio.ResponseBenefitDTO;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record CheckoutOutputDTO(
        UUID ordineId,
        String numeroOrdine,
        LocalDate dataOrdine,
        String statoOrdine,
        BigDecimal importoTotale,
        ResponseUserAddressDTO indirizzoSpedizione,
        LocalDate dataConsegnaStimata,
        List<ProductOrderOutputDTO> prodottiOrdinati,
        ResponseBenefitDTO vantaggio,
        int puntiTotaliUtente
)implements Serializable {}
