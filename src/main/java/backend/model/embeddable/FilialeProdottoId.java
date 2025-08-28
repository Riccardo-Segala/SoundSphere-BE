package backend.model.embeddable;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Embeddable
public class FilialeProdottoId {
    private UUID idFiliale;
    private UUID idProdotto;

    public FilialeProdottoId() {}

    public FilialeProdottoId(UUID idFiliale, UUID idProdotto) {
        this.idFiliale = idFiliale;
        this.idProdotto = idProdotto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FilialeProdottoId that)) return false;
        return idFiliale.equals(that.idFiliale) && idProdotto.equals(that.idProdotto);
    }

    @Override
    public int hashCode() {
        return 31 * idFiliale.hashCode() + idProdotto.hashCode();
    }
}
