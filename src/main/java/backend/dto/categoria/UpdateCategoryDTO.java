package backend.dto.categoria;

import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO per l'aggiornamento di una categoria esistente.
 * I campi sono opzionali per permettere aggiornamenti parziali.
 */
public record UpdateCategoryDTO(

        @Size(min = 2, max = 100, message = "Il nome deve avere tra 2 e 100 caratteri.")
        String name,

        UUID parentId
) implements Serializable {}