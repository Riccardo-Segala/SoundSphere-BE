package backend.dto.dati_statici;

import java.io.Serializable;

public record DeliveryLimitDataDTO(
        double costoSpedizione,
        double sogliaSpedizioneGratuita
)implements Serializable {}
