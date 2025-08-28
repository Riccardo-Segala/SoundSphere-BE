package backend.dto.filiale;



import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link backend.model.Filiale}
 */
public record ResponseBranchDTO (
        UUID id,
        String nome,
        String telefono,
        String email,
        BranchAddressDTO indirizzo
) implements Serializable {
}