package backend.dto.dipendente;

import backend.model.enums.Sesso;
import backend.model.enums.Tipologia;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

/**
 * DTO for {@link backend.model.Dipendente}
 */

public record ResponseEmployeeDTO (
        String nome,
        String cognome,
        String email,
        LocalDate dataDiNascita,
        Tipologia tipologia,
        String pathImmagine,
        Sesso sesso,
        LocalDate dataRegistrazione,
        Integer punti,
        UUID vantaggioId,
        double stipendio,
        LocalDate scadenzaContratto,
        LocalDate dataAssunzione,
        UUID filialeId
) implements Serializable {
}