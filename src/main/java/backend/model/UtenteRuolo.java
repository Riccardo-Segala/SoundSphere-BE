package backend.model; // Assicurati che il package sia corretto

import backend.model.embeddable.FilialeProdottoId;
import backend.model.embeddable.UtenteRuoloId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "utente_ruolo") // Dichiara che questa entità usa UtenteRuoloId come chiave
public class UtenteRuolo {
   @EmbeddedId
    private UtenteRuoloId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idUtente")
    @JoinColumn(name = "id_utente") // Mappa alla colonna FK
    private Utente utente;


    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idRuolo")
    @JoinColumn(name = "id_ruolo") // Mappa alla colonna FK
    private Ruolo ruolo;

    // La tua nuova colonna con le informazioni aggiuntive
    @Column(name = "data_scadenza")
    private LocalDate dataScadenza;

    public UtenteRuolo() {
        this.id = new UtenteRuoloId();
    }

}