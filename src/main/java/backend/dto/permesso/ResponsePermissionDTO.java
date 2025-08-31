package backend.dto.permesso;

import java.io.Serializable;
import java.util.UUID;

public record ResponsePermissionDTO (
        UUID id,
        String nome
)implements Serializable {}
