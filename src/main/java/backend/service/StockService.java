package backend.service;

import backend.exception.OutOfStockException;
import backend.model.Filiale;
import backend.model.Stock;
import backend.model.embeddable.FilialeProdottoId;
import backend.repository.FilialeRepository;
import backend.repository.StockRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.List;

@Service
public class StockService extends GenericService<Stock, FilialeProdottoId> {
    private final StockRepository stockRepository;
    private final FilialeRepository filialeRepository;

    @Autowired
    public StockService(StockRepository repository, StockRepository stockRepository, FilialeRepository filialeRepository) {
        super(stockRepository); // Passa il repository al costruttore della classe base
        this.stockRepository = stockRepository;
        this.filialeRepository = filialeRepository;
    }

    @Transactional
    public void reserveStock(String nomeFiliale, UUID prodottoId, int quantitaRichiesta) {
        Filiale filiale = filialeRepository.findByName(nomeFiliale)
                .orElseThrow(() -> new EntityNotFoundException("Filiale non trovata: " + nomeFiliale));

        FilialeProdottoId stockId = new FilialeProdottoId(filiale.getId(), prodottoId);

        Stock stock = stockRepository.findById(stockId)
                .orElseThrow(() -> new EntityNotFoundException("Stock non trovato per il prodotto: " + prodottoId));

        if (stock.getQuantita() < quantitaRichiesta) {
            throw new OutOfStockException("Quantità non disponibile per il prodotto: " + prodottoId);
        }

        stock.setQuantita(stock.getQuantita() - quantitaRichiesta);
        // Il salvataggio avverrà automaticamente alla fine della transazione @Transactional
    }

    // Restituisce una lista di stringhe con i nomi delle marche disponibili nella filiale "online"
    public List<String> getMarcheDisponibiliOnline() {

        return stockRepository.findDistinctMarcaByFilialeNome("online");
    }
}