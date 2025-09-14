package backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "noleggio")
public class Noleggio {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    private LocalDate dataInizio;

    private LocalDate dataScadenza;

    private LocalDate dataRestituzione;

    private LocalDate dataPagamento;

    private double totale;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_utente", nullable = false)
    private Utente utente;

    @ManyToOne(fetch = FetchType.LAZY) // CAMBIAMENTO: Aggiunto LAZY per coerenza
    @JoinColumn(name = "id_indirizzo_utente")
    private IndirizzoUtente indirizzo;

    @OneToMany(mappedBy = "noleggio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DettagliNoleggio> dettagli = new ArrayList<>();

}