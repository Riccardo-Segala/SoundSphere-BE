package backend.dto.utente;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

public record RoleAssignmentDTO(
        @NotNull
        UUID userId,
        @NotNull
        @Future
        LocalDate expirationDate
)implements Serializable {}
