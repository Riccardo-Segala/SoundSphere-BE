package backend.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@DiscriminatorValue("ORGANIZZATORE_EVENTI")
@PrimaryKeyJoinColumn(name = "id")
public class OrganizzatoreEventi extends Utente {

    private LocalDate dataScadenza;
}
