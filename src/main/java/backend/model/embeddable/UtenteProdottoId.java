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
public class UtenteProdottoId implements Serializable {
    private UUID utenteId;
    private UUID prodottoId;

    public UtenteProdottoId() {}

    public UtenteProdottoId(UUID utenteId, UUID prodottoId) {
        this.utenteId = utenteId;
        this.prodottoId = prodottoId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UtenteProdottoId that)) return false;
        return Objects.equals(utenteId, that.utenteId) &&
                Objects.equals(prodottoId, that.prodottoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(utenteId, prodottoId);
    }
}
