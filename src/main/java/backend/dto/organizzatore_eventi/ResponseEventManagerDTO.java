package backend.dto.organizzatore_eventi;

import backend.model.enums.Sesso;
import backend.model.enums.Tipologia;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

/**
 * DTO for {@link backend.model.OrganizzatoreEventi}
 */
public record ResponseEventManagerDTO (
        UUID id,
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
        LocalDate dataScadenza
)implements Serializable {
}