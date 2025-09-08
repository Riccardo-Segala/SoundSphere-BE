package backend.dto.utente;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

public record UserIdListDTO(
        List<UUID> userIdList
)implements Serializable {}
