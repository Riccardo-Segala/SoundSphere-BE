package backend.dto.ruolo;

import backend.dto.permesso.ResponsePermissionDTO;

import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

public record ResponseRoleDTO(
        UUID id,
        String nome,
        Set<ResponsePermissionDTO> permessi
) implements Serializable {}
