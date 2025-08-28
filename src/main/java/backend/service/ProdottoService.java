package backend.service;

import backend.dto.prodotto.CatalogProductDTO;
import backend.dto.prodotto.ResponseProductDTO;
import backend.exception.ResourceNotFoundException;
import backend.mapper.ProductMapper;
import backend.model.Filiale;
import backend.model.Prodotto;
import backend.model.Stock;
import backend.repository.ProdottoRepository;
import backend.repository.RecensioneRepository;
import backend.repository.StockRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProdottoService extends GenericService<Prodotto, UUID> {
    private final ProdottoRepository repository;
    private final RecensioneRepository recensioneRepository;
    private final ProductMapper productMapper;
    private final FilialeService filialeService;
    private final StockRepository stockRepository;

    @Value("${app.filiale.online.name}")
    private String nomeFilialeOnline;

    public ProdottoService(ProdottoRepository repository, RecensioneRepository recensioneRepository, ProductMapper productMapper, FilialeService filialeService, StockRepository stockRepository) {
        super(repository); // Passa il repository al costruttore della classe base
        this.repository = repository;
        this.recensioneRepository = recensioneRepository;
        this.productMapper = productMapper;
        this.filialeService = filialeService;
        this.stockRepository = stockRepository;
    }

    public ResponseProductDTO getProductById(UUID id) {
        Prodotto prodotto = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Prodotto non trovato con ID: " + id));
        return productMapper.toDto(prodotto);
    }

    public Double getAverageStars(UUID prodottoId) {
        // Verifica prima che il prodotto esista, per restituire un 404 se l'ID è invalido
        if (!repository.existsById(prodottoId)) {
            throw new EntityNotFoundException("Prodotto non trovato con ID: " + prodottoId);
        }
        // Chiama il metodo del repository delle recensioni
        return recensioneRepository.findAverageStarsByProdottoId(prodottoId);
    }

    @Transactional(readOnly = true)
    public List<ResponseProductDTO> findProductsByCategoryId(UUID categoryId) {
        if (categoryId == null) {
            return List.of(); // o gestisci come preferisci
        }
        List<Prodotto> prodotti = repository.findByExactCategoryId(categoryId);
        return prodotti.stream()
                .map(productMapper::toDto)
                .toList();
    }

    public List<CatalogProductDTO> getOnlineProductCatalog() {
        // 1. Trova la filiale online
        Filiale onlineBranch = filialeService.getOnlineBranch();

        // 2. Chiama il repository per ottenere i dati grezzi (entità)
        List<Object[]> results = repository.findAllProductsWithStockInfoByBranchId(onlineBranch.getId());

        // 3. Usa il mapper per trasformare ogni riga di risultato nel DTO finale
        return results.stream()
                .map(row -> {
                    Prodotto product = (Prodotto) row[0];
                    Stock stock = (Stock) row[1]; // Può essere null
                    double averageStars = this.getAverageStars(product.getId());
                    return productMapper.toCatalogDTO(product, stock, averageStars);
                })
                .collect(Collectors.toList());
    }

    public List<ResponseProductDTO> getProductInStockByBranchId(UUID filialeId) {
        List<Prodotto> prodotti = repository.getProductInStockByBranchId(filialeId);
        return prodotti.stream().map(productMapper::toDto).collect(Collectors.toList());
    }

    public List<String> getAvailableBrandsOnline() {
        return stockRepository.findDistinctMarcaByFilialeNome(nomeFilialeOnline);
    }
}
