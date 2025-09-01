package backend.model.embeddable;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Embeddable
public class UtenteRuoloId implements Serializable {
    private UUID idUtente;
    private UUID idRuolo;

    public UtenteRuoloId() {}

    public UtenteRuoloId(UUID idUtente, UUID idRuolo) {
        this.idUtente = idUtente;
        this.idRuolo = idRuolo;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UtenteRuoloId that)) return false;
        return Objects.equals(idUtente, that.idUtente) &&
                Objects.equals(idRuolo, that.idRuolo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idUtente, idRuolo);
    }
}