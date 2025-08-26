package backend.dto.prodotto;

import java.io.Serializable;
import java.util.UUID;

public record CatalogProductDTO(
        UUID id,
        String nome,
        String marca, // Aggiunto
        double prezzo,
        String pathImmagine,
        double stelleMedie,
        int quantitaDisponibile,
        double costoGiornaliero,
        int quantitaDisponibileAlNoleggio
) implements Serializable {}
