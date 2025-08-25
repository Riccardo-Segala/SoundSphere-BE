package backend.dto.categoria;

import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

/**
 * DTO per i dettagli di una singola categoria, incluse le sue figlie DIRETTE.
 * Usato per la navigazione a "drill-down". NON è ricorsivo.
 */
public record ResponseCategoryNavigationDTO(
        UUID id,
        String name,
        String slug,

        // Contiene solo un riassunto dei figli, non l'intero oggetto
        Set<ResponseParentCategoryDTO> children,

        // Flag che dice al frontend se è arrivato alla fine della navigazione
        boolean isLeaf
) implements Serializable {}