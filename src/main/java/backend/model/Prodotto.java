package backend.model;

import jakarta.persistence.*; // Aggiunto per GeneratedValue
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "prodotto")
public class Prodotto {
    @Id
    // --- CAMBIAMENTO CRITICO: Aggiunta generazione automatica dell'ID ---
    @GeneratedValue(strategy = GenerationType.UUID)
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