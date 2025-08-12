package backend.dto.utente;

import backend.model.enums.Sesso;
import backend.model.enums.Tipologia;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

/**
 * DTO for {@link backend.model.Utente}
 */

public record CreateUserDTO(
        String nome,
        String cognome,
        String email,
        String password,
        LocalDate dataDiNascita,
        String pathImmagine,
        Sesso sesso
) implements Serializable {
}