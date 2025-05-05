package backend.dto.filiale;



import java.io.Serializable;

/**
 * DTO for {@link backend.model.Filiale}
 */
public record CreateBranchDTO (
        String indirizzo,
        String nome,
        String telefono,
        String email
) implements Serializable {
}