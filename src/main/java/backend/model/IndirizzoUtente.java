package backend.model;

import backend.model.enums.TipologiaIndirizzo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "indirizzo_utente")
public class IndirizzoUtente {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;


    private String via;
    private String civico;
    private String cap;
    private String citta;
    private String provincia;
    private String nazione;

    @Column(name = "is_default")
    private boolean isDefault;

    @Enumerated(EnumType.STRING)
    private TipologiaIndirizzo tipologia;

    @ManyToOne
    @JoinColumn(name = "id_utente", nullable = false)
    private Utente utente;


}