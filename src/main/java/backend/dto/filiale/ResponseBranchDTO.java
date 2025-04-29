package backend.dto.filiale;

import lombok.Value;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link backend.model.Filiale}
 */
@Value
public class ResponseBranchDTO implements Serializable {
    UUID id;
    String indirizzo;
    String nome;
    String telefono;
    String email;
}