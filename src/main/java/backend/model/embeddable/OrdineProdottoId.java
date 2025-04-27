package backend.model.embeddable;

import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Embeddable
public class OrdineProdottoId implements Serializable{
    private UUID ordineId;
    private UUID prodottoId;

    public OrdineProdottoId() {}

    public OrdineProdottoId(UUID ordineId, UUID prodottoId) {
        this.ordineId = ordineId;
        this.prodottoId = prodottoId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrdineProdottoId that)) return false;
        return ordineId.equals(that.ordineId) && prodottoId.equals(that.prodottoId);
    }

    @Override
    public int hashCode() {
        return 31 * ordineId.hashCode() + prodottoId.hashCode();
    }
}
