package backend.dto.utente;

import backend.model.enums.Sesso;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

/**
 * DTO for {@link backend.model.Utente}
 */
public record UpdateUserDTO(
        UUID id,
        String nome,
        String cognome,
        String password,
        LocalDate dataDiNascita,
        String pathImmagine,
        Sesso sesso
) implements Serializable {
}