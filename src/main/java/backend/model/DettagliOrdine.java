package backend.model;
import backend.model.embeddable.OrdineProdottoId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "dettagli_ordine")
public class DettagliOrdine {

    @EmbeddedId
    private OrdineProdottoId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("ordineId")
    @JoinColumn(name = "id_ordine")
    private Ordine ordine;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("prodottoId")
    @JoinColumn(name = "id_prodotto")
    private Prodotto prodotto;

    private int quantita;

}