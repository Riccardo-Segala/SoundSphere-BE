package backend.dto.filiale;



import java.io.Serializable;

/**
 * DTO for {@link backend.model.Filiale}
 */
public record UpdateBranchDTO (
        String nome,
        String telefono,
        String email,
        String via,
        String citta,
        String cap,
        String provincia,
        String nazione
) implements Serializable {
}