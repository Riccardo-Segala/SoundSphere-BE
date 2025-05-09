package backend.dto.vantaggio;



import java.io.Serializable;

/**
 * DTO for {@link backend.model.Vantaggio}
 */
public record CreateBenefitDTO (
        String nome,
        Double sconto,
        Integer punteggioMinimo,
        Integer punteggioMassimo
) implements Serializable {
}