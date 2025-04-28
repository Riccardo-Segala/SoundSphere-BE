package backend.dto.filiale;

import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link backend.model.Filiale}
 */
@Value
public class CreateBranchDTO implements Serializable {
    String indirizzo;
    String nome;
    String telefono;
    String email;
}