package backend.model;

import backend.model.embeddable.UtenteProdottoId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "carrello")
public class Carrello {
    @EmbeddedId
    private UtenteProdottoId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("utenteId")
    @JoinColumn(name = "id_utente")
    private Utente utente;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("prodottoId")
    @JoinColumn(name = "id_prodotto")
    private Prodotto prodotto;

    @Column(nullable = false)
    private int quantita;

    private boolean wishlist;

    public Carrello() {
    }

    // costruttore per i test
    public Carrello(Utente utente, Prodotto prodotto, int quantita) {

        this.id = new UtenteProdottoId(utente.getId(), prodotto.getId());


        this.utente = utente;
        this.prodotto = prodotto;


        this.quantita = quantita;
        this.wishlist = false;
    }

}