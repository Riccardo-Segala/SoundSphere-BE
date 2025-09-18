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

    // Costruttore vuoto, richiesto da JPA
    public Carrello() {
    }

    // Il nostro costruttore "di comodo" per i test
    public Carrello(Utente utente, Prodotto prodotto, int quantita) {
        // Creiamo e impostiamo l'ID composto
        this.id = new UtenteProdottoId(utente.getId(), prodotto.getId());

        // Impostiamo le relazioni
        this.utente = utente;
        this.prodotto = prodotto;

        // Impostiamo i dati rimanenti
        this.quantita = quantita;
        this.wishlist = false; // Valore di default
    }

}