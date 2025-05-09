package backend.model;

import backend.model.embeddable.FilialeProdottoId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "stock")
public class Stock {

    @EmbeddedId
    private FilialeProdottoId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("filialeId")
    @JoinColumn(name = "id_filiale")
    private Filiale filiale;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("prodottoId")
    @JoinColumn(name = "id_prodotto")
    private Prodotto prodotto;

    private int quantita;
    private int quantitaPerNoleggio;


}