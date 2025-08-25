package backend.dto.categoria;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO per la creazione di una nuova categoria.
 * Contiene i dati minimi richiesti dal client.
 */
public record CreateCategoryDTO(

        @NotBlank(message = "Il nome della categoria non può essere vuoto.")
        @Size(min = 2, max = 100, message = "Il nome deve avere tra 2 e 100 caratteri.")
        String name,

        // L'ID della categoria genitore. Può essere nullo se è una categoria principale.
        UUID parentId
) implements Serializable {}