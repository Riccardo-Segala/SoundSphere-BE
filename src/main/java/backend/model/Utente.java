package backend.model;

import backend.model.enums.Sesso;
import backend.model.enums.Tipologia;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "utente")
@Inheritance(strategy = InheritanceType.JOINED) // Strategia JOINED (tabelle separate per le sotto-classi)
@DiscriminatorColumn(name = "tipologia", discriminatorType = DiscriminatorType.STRING) // Nome della colonna discriminante
public class Utente {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    private String nome;

    private String cognome;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private LocalDate dataDiNascita;

    @Column(name = "tipologia", insertable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    private Tipologia tipologia;

    @Column(columnDefinition = "TEXT")
    private String pathImmagine;

    @Enumerated(EnumType.STRING)
    private Sesso sesso;

    private LocalDate dataRegistrazione;

    private Integer punti;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_vantaggio")
    private Vantaggio vantaggio;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_indirizzo_utente")
    private IndirizzoUtente indirizzo;

}