package backend.dto.vantaggio;

import lombok.Value;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link backend.model.Vantaggio}
 */
@Value
public class UpdateBenefitDTO implements Serializable {
    UUID id;
    String nome;
    Double sconto;
    Integer punteggioMinimo;
    Integer punteggioMassimo;
}