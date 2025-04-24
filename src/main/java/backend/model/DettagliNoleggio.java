package backend.model;

import backend.model.embeddable.NoleggioProdottoId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "dettagli_noleggio")
public class DettagliNoleggio {

    @EmbeddedId
    private NoleggioProdottoId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("noleggioId")
    @JoinColumn(name = "id_noleggio")
    private Noleggio noleggio;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("prodottoId")
    @JoinColumn(name = "id_prodotto")
    private Prodotto prodotto;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_organizzatore_eventi", referencedColumnName = "id")
    private OrganizzatoreEventi organizzatoreEventi;

    private int quantita;
}