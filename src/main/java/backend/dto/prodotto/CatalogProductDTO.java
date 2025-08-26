package backend.dto.prodotto;

import java.io.Serializable;
import java.util.UUID;

public record CatalogProductDTO(
        UUID id,
        String nome,
        String marca, // Aggiunto
        double prezzo,
        String pathImmagine,
        int quantitaDisponibile,
        int quantitaDisponibileAlNoleggio
) implements Serializable {}
