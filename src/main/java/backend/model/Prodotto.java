package backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "prodotto")
public class Prodotto {
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    private String nome;

    private String descrizione;

    @Column(nullable = false)
    private double prezzo;

    private String marca;

    @Column(name = "is_rentable")
    private boolean isRentable;

    @Column(name = "costo_giornaliero")
    private double costoGiornaliero;

    @Column(columnDefinition = "TEXT")
    private String pathImmagine;

}