package backend.model.embeddable;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Embeddable
public class FilialeProdottoId {
    private UUID filialeId;
    private UUID prodottoId;

    public FilialeProdottoId() {}

    public FilialeProdottoId(UUID filialeId, UUID prodottoId) {
        this.filialeId = filialeId;
        this.prodottoId = prodottoId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FilialeProdottoId that)) return false;
        return filialeId.equals(that.filialeId) && prodottoId.equals(that.prodottoId);
    }

    @Override
    public int hashCode() {
        return 31 * filialeId.hashCode() + prodottoId.hashCode();
    }
}
