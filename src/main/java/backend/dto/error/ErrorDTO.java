package backend.dto.error;

import java.time.LocalDateTime;

public record ErrorDTO(
        int status,
        String error,
        String message,
        LocalDateTime timestamp
) {}
