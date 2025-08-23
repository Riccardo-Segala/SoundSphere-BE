package backend.dto.recensione;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link backend.model.Recensione}
 */
public record CreateReviewDTO(
        @NotNull
        UUID prodottoId,
        @Min(1) @Max(5)
        int numStelle,
        String titolo,
        @NotBlank
        String descrizione
)implements Serializable {
}