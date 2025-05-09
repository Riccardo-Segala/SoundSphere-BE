package backend.dto.vantaggio;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link backend.model.Vantaggio}
 */
public record ResponseBenefitDTO (
        UUID id,
        String nome,
        Double sconto,
        Integer punteggioMinimo,
        Integer punteggioMassimo
) implements Serializable {
}