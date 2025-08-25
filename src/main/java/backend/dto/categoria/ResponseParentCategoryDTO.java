package backend.dto.categoria;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO leggero per rappresentare una sintesi di una categoria.
 * Usato per le relazioni (es. genitore) o in liste per evitare
 * di caricare l'intera struttura gerarchica.
 */
public record ResponseParentCategoryDTO(
        UUID id,
        String name,
        String slug
) implements Serializable {}