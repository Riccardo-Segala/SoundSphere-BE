package backend.dto.categoria;

import java.io.Serializable;
import java.util.UUID;

public record ResponseCategoryDTO(
        UUID id,
        String name,
        String slug,
        ResponseParentCategoryDTO parent
) implements Serializable {}
