package backend.dto.utente;

import backend.dto.vantaggio.ResponseBenefitDTO;
import backend.model.enums.Sesso;
import backend.model.enums.Tipologia;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

/**
 * DTO for {@link backend.model.Utente}
 */
public record ResponseUserDTO(
        String nome,
        String cognome,
        String email,
        LocalDate dataDiNascita,
        Tipologia tipologia,
        String pathImmagine,
        Sesso sesso,
        LocalDate dataRegistrazione,
        Integer punti,
        ResponseBenefitDTO vantaggio
) implements Serializable {
}