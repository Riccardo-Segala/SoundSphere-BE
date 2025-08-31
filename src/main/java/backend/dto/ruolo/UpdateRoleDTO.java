package backend.dto.ruolo;

import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

public record UpdateRoleDTO(
        @Size(min = 3, max = 50, message = "Il nome deve avere tra 3 e 50 caratteri")
        String nome,
        Set<UUID> permessiIds
)implements Serializable {}
