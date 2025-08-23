package backend.dto.recensione;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

/**
 * DTO for {@link backend.model.Recensione}
 */
public record UpdateReviewDTO(
        @Min(1) @Max(5)
        int numStelle,
        String titolo,
        @NotBlank
        String descrizione
) implements Serializable {
}