package backend.dto.utente.admin;

import backend.model.enums.Sesso;
import backend.model.enums.Tipologia;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

/**
 * DTO for {@link backend.model.Utente}
 */
public record UpdateUserFromAdminDTO(
        String nome,
        String cognome,
        String email,
        String password,
        LocalDate dataDiNascita,
        Tipologia tipologia,
        String pathImmagine,
        Sesso sesso,
        Integer punti,
        UUID vantaggioId,
        Set<UUID> ruoliIds
) implements Serializable {}