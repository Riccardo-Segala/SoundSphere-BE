package backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "ruolo")
public class Ruolo {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String nome;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "ruolo_permesso",
            joinColumns = @JoinColumn(name = "id_ruolo"),
            inverseJoinColumns = @JoinColumn(name = "id_permesso")
    )
    private Set<Permesso> permessi = new HashSet<>();
}