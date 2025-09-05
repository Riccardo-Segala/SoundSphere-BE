package backend.dto.dipendente.admin;
import backend.dto.utente.admin.CreateUserFromAdminDTO;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.LocalDate;
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
)implements Serializable{}
