package backend.service;

import backend.model.Carrello;
import backend.model.DatiStatici;
import backend.model.embeddable.UtenteProdottoId;
import backend.repository.CarrelloRepository;
import backend.repository.DatiStaticiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CarrelloService extends GenericService<Carrello, UtenteProdottoId> {
    public CarrelloService(CarrelloRepository repository) {
        super(repository); // Passa il repository al costruttore della classe base
    }

    @Autowired
    private CarrelloRepository carrelloRepository;
    @Autowired
    private DatiStaticiRepository datiStaticiRepository;
    @Autowired
    private VantaggioService vantaggioService;

    // Calcola il totale parziale del carrello per un utente specifico
    public double calcolaTotaleParziale(UUID utenteId) {
        //Recupera tutte le righe del carrello per l'utente dal repository
        List<Carrello> righeCarrello = carrelloRepository.findCartByUserId(utenteId);

        if (righeCarrello.isEmpty()) {
            return 0.0;
        }

        double totaleParziale = 0.0;

        for (Carrello riga : righeCarrello) {
            totaleParziale += riga.getProdotto().getPrezzo() * riga.getQuantita();
        }

        return totaleParziale;
    }

    //Calcola il totale finale del carrello con le spese di spedizione ed eventuali sconti
    public double calcolaTotaleFinale(UUID utenteId) {
        double totParziale = calcolaTotaleParziale(utenteId);

        Optional<DatiStatici> spedizioneDati = datiStaticiRepository.findByNome("spedizione");

        // Se il valore non è presente, assumiamo che sia 0.0
        double speseDiSpedizione = spedizioneDati.map(DatiStatici::getValore).orElse(0.0);

        double sconto = vantaggioService.calcolaSconto(utenteId);

        double totaleFinale = totParziale + speseDiSpedizione - sconto;

        // Totale non negativo
        return Math.max(0.0, totaleFinale);
    }

    @Transactional
    public void deleteAllItems(List<Carrello> carrelli) {
        // Questo metodo lavora con le entità gestite,
        // mantenendo il contesto di persistenza sincronizzato.
        carrelloRepository.deleteAll(carrelli);
    }

    // aggiunta di un elemento al carrello o alla wishlist

    public List<Carrello> getWishlistByUtenteId(UUID utenteId) {
        return carrelloRepository.findWishlistByUserId(utenteId);
    }

    public List<Carrello> getCartByUtenteId(UUID utenteId) {
        return carrelloRepository.findCartByUserId(utenteId);
    }

}

