package backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@DiscriminatorValue("DIPENDENTE")
@PrimaryKeyJoinColumn(name = "id")
public class Dipendente extends Utente {

    private double stipendio;

    private LocalDate scadenzaContratto;

    private LocalDate dataAssunzione;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_filiale", nullable = false)
    private Filiale filiale;

}
