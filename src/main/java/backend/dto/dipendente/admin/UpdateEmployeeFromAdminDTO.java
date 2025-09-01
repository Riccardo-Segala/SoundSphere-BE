package backend.dto.dipendente.admin;

import backend.dto.common.HasRole;
import backend.model.enums.Sesso;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

public record UpdateEmployeeFromAdminDTO(
        String nome,
        String cognome,
        String email,
        String password,
        LocalDate dataDiNascita,
        String pathImmagine,
        Sesso sesso,
        Set<UUID> ruoliIds,
        Double stipendio,
        LocalDate scadenzaContratto,
        LocalDate dataAssunzione,
        UUID filialeId
) implements Serializable, HasRole {}
