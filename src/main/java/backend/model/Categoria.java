package backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "categoria")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "slug", nullable = false, unique = true)
    private String slug;

    // --- RELAZIONE GERARCHICA ---
    // Molte categorie (figlie) possono avere un solo genitore.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Categoria parent;

    // Una categoria (genitore) può avere molte categorie figlie.
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Categoria> children = new HashSet<>();


    // --- RELAZIONE CON PRODOTTO ---
    // mappedBy indica che la relazione è "posseduta" dall'altra parte (dal campo "categorie" in Prodotto).
    // Questo lato è l'inverso, quindi non gestisce la tabella di join.
    @ManyToMany(mappedBy = "categorie")
    private Set<Prodotto> prodotti = new HashSet<>();

}