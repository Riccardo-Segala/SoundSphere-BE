package backend.dto.carrello;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

public record DeleteCartDTO(
        List<UUID> productIds
) implements Serializable {}