package backend.dto.dipendente;

import backend.dto.utente.CreateUserDTO;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

/**
 * DTO for {@link backend.model.Dipendente}
 */
public record CreateEmployeeDTO(
        CreateUserDTO utente,
        double stipendio,
        LocalDate scadenzaContratto,
        LocalDate dataAssunzione,
        UUID filialeId
) implements Serializable {
}