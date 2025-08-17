package backend.dto.autenticazione;

import java.util.List;
import java.util.UUID;

public record JwtResponseDTO(
        String token,
        Long expiresIn,
        UUID id,
        String nome,
        String cognome,
        String email,
        List<String> ruoli
) {}
