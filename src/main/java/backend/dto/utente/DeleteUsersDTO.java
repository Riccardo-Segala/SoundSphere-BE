package backend.dto.utente;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

public record DeleteUsersDTO(
        List<UUID> userIds
) implements Serializable {}
