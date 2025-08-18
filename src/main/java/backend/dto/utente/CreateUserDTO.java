package backend.dto.utente;

import backend.model.enums.Sesso;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

/**
 * DTO for {@link backend.model.Utente}
 */
public record CreateUserDTO(
        @NotNull
        String nome,
        @NotNull
        String cognome,
        @NotNull
        String email,
        @NotNull
        String password,
        @NotNull
        LocalDate dataDiNascita,
        String pathImmagine,
        @NotNull
        Sesso sesso
) implements Serializable {
}