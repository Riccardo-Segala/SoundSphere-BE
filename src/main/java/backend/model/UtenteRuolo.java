package backend.model; // Assicurati che il package sia corretto

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "utente_ruolo") // Dichiara che questa entità usa UtenteRuoloId come chiave
public class UtenteRuolo {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID) //
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_utente", nullable = false)
    private Utente utente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ruolo", nullable = false)
    private Ruolo ruolo;

    @Column(name = "data_scadenza")
    private LocalDate dataScadenza;


    public UtenteRuolo(Utente utente, Ruolo ruolo, LocalDate dataScadenza) {
        this.utente = utente;
        this.ruolo = ruolo;
        this.dataScadenza = dataScadenza;
    }
}