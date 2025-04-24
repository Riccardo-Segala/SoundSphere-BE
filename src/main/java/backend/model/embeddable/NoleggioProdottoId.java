package backend.model.embeddable;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
public class NoleggioProdottoId implements Serializable {
    private UUID noleggioId;
    private UUID prodottoId;

    public NoleggioProdottoId() {}

    public NoleggioProdottoId(UUID noleggioId, UUID prodottoId) {
        this.noleggioId = noleggioId;
        this.prodottoId = prodottoId;
    }

    public UUID getNoleggioId() {
        return noleggioId;
    }

    public UUID getProdottoId() {
        return prodottoId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NoleggioProdottoId that)) return false;
        return Objects.equals(noleggioId, that.noleggioId) &&
               Objects.equals(prodottoId, that.prodottoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(noleggioId, prodottoId);
    }
}