package backend.dto.dipendente.admin;

import backend.dto.common.HasRole;
import backend.dto.utente.admin.CreateUserFromAdminDTO;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;

public record CreateEmployeeFromAdminDTO(

        CreateUserFromAdminDTO utente,
        @NotNull
        Double stipendio,
        LocalDate scadenzaContratto,
        @NotNull
        LocalDate dataAssunzione,
        @NotNull
        UUID filialeId
)implements Serializable, HasRole {
    @Override
    public Set<UUID> ruoliIds() {
        return utente != null ? utente.ruoliIds() : Collections.EMPTY_SET;
    }
}
