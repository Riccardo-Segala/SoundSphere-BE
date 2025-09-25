package backend.model;

import jakarta.persistence.*; // Aggiunto per GeneratedValue
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
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

    @Column(columnDefinition = "TEXT")
    private String descrizione;

    @Column(nullable = false)
    private double prezzo;

    private String marca;

    @Column(name = "is_rentable")
    private boolean rentable;

    @Column(name = "costo_giornaliero")
    private double costoGiornaliero;

    @Column(columnDefinition = "TEXT")
    private String pathImmagine;

    @ManyToMany(fetch = FetchType.LAZY) // LAZY per non caricare le categorie se non servono
    @JoinTable(
            name = "prodotto_categoria", // Nome della tabella di join nel DB
            joinColumns = @JoinColumn(name = "prodotto_id"), // FK verso questa entità (Prodotto)
            inverseJoinColumns = @JoinColumn(name = "categoria_id") // FK verso l'altra entità (Categoria)
    )
    private Set<Categoria> categorie = new HashSet<>();
}