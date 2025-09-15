package backend.dto.checkout;

import backend.dto.indirizzo_utente.ResponseUserAddressDTO;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record CheckoutOutputRentalDTO(
        UUID noleggioId,
        String numeroNoleggio,
        LocalDate dataInizio,
        LocalDate dataScadenza,
        double importoTotale,
        ResponseUserAddressDTO indirizzoSpedizione,
        List<ProductOrderOutputDTO> prodottiNoleggiati
) implements Serializable {}