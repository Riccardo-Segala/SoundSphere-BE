package backend.dto.vantaggio;

import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link backend.model.Vantaggio}
 */
@Value
public class CreateBenefitDTO implements Serializable {
    String nome;
    Double sconto;
    Integer punteggioMinimo;
    Integer punteggioMassimo;
}