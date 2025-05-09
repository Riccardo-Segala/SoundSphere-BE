package backend.service;

import backend.model.Stock;
import backend.model.embeddable.FilialeProdottoId;
import backend.repository.StockRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class StockService extends GenericService<Stock, FilialeProdottoId> {

    public StockService(StockRepository repository) {
        super(repository); // Passa il repository al costruttore della classe base
    }
}