package backend.service;

import backend.dto.prodotto.ResponseProductDTO;
import backend.dto.stock.ResponseStockDTO;
import backend.exception.OutOfStockException;
import backend.mapper.ProductMapper;
import backend.mapper.StockMapper;
import backend.model.Filiale;
import backend.model.Prodotto;
import backend.model.Stock;
import backend.model.embeddable.FilialeProdottoId;
import backend.repository.FilialeRepository;
import backend.repository.StockRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StockService extends GenericService<Stock, FilialeProdottoId> {
    private final StockRepository stockRepository;
    private final FilialeRepository filialeRepository;
    private final FilialeService filialService;
    private final ProductMapper productMapper;
    private final StockMapper stockMapper;


    @Autowired
    public StockService(StockRepository stockRepository, FilialeRepository filialeRepository, FilialeService filialService, ProductMapper productMapper, StockMapper stockMapper) {
        super(stockRepository); // Passa il repository al costruttore della classe base
        this.stockRepository = stockRepository;
        this.filialeRepository = filialeRepository;
        this.filialService = filialService;
        this.productMapper = productMapper;
        this.stockMapper = stockMapper;
    }

    @Transactional
    public void reserveStock(String nomeFiliale, UUID prodottoId, int quantitaRichiesta) {
        Filiale filiale = filialService.getByName(nomeFiliale);

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


    public List<ResponseStockDTO> getOnlineStock() {
        List<Stock> onlineStock = getOnlineStockEntries();
        return onlineStock.stream()
                .map(stockMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Private helper method that retrieves the raw Stock entities from the repository.
     */
    private List<Stock> getOnlineStockEntries() {
        Filiale onlineBranch = filialService.getOnlineBranch();
        return stockRepository.findByFilialeId(onlineBranch.getId()); // Assumendo findByBranchId
    }

    public int getOnlineStockProductQuantity(UUID prodottoId) {
        // 1. Trova l'entità della filiale "online" (riutilizzando il nostro metodo helper)
        Filiale filialeOnline = filialService.getOnlineBranch();

        // 2. Crea l'ID composito per la tabella stock
        FilialeProdottoId stockId = new FilialeProdottoId(filialeOnline.getId(), prodottoId);

        // 3. Cerca lo stock nel repository.
        // Se lo trova, estrae la quantità. Altrimenti, restituisce 0.
        return stockRepository.findById(stockId)
                .map(Stock::getQuantita)
                .orElse(0);
    }


}