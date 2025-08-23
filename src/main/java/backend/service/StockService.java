package backend.service;

import backend.dto.prodotto.ResponseProductDTO;
import backend.exception.OutOfStockException;
import backend.mapper.ProductMapper;
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

    @Value("${app.filiale.online.name}")
    private String nomeFilialeOnline;

    @Autowired
    public StockService(StockRepository stockRepository, FilialeRepository filialeRepository, FilialeService filialService, ProductMapper productMapper) {
        super(stockRepository); // Passa il repository al costruttore della classe base
        this.stockRepository = stockRepository;
        this.filialeRepository = filialeRepository;
        this.filialService = filialService;
        this.productMapper = productMapper;
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
    public List<String> getMarcheDisponibiliOnline() {

        return stockRepository.findDistinctMarcaByFilialeNome("online");
    }


    public List<Prodotto> getProductInStockByBranchId(UUID filialeId) {
        return filialeRepository.getProductInStockByBranchId(filialeId);
    }

    public List<ResponseProductDTO> getOnlineStock() {
        List<Prodotto> prodottiInStock = getOnlineProduct();
        return prodottiInStock.stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
    }

    private List<Prodotto> getOnlineProduct() {
        Filiale filialeOnline = this.getOnlineBranch();
        return getProductInStockByBranchId(filialeOnline.getId());
    }

    public int getOnlineStockProductQuantity(UUID prodottoId) {
        // 1. Trova l'entità della filiale "online" (riutilizzando il nostro metodo helper)
        Filiale filialeOnline = this.getOnlineBranch();

        // 2. Crea l'ID composito per la tabella stock
        FilialeProdottoId stockId = new FilialeProdottoId(filialeOnline.getId(), prodottoId);

        // 3. Cerca lo stock nel repository.
        // Se lo trova, estrae la quantità. Altrimenti, restituisce 0.
        return stockRepository.findById(stockId)
                .map(Stock::getQuantita)
                .orElse(0);
    }

    private Filiale getOnlineBranch() {
        return filialService.getByName(nomeFilialeOnline);
    }

}