package backend.dto.filiale;



import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link backend.model.Filiale}
 */
public record UpdateBranchDTO (
        UUID id,
        String indirizzo,
        String nome,
        String telefono,
        String email
) implements Serializable {
}