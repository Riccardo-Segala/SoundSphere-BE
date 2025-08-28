package backend.service.admin;


import backend.dto.prodotto.CreateProductDTO;
import backend.dto.prodotto.ResponseProductDTO;
import backend.dto.prodotto.UpdateProductDTO;
import backend.exception.ResourceNotFoundException;
import backend.mapper.ProductMapper;
import backend.mapper.StockMapper;
import backend.model.Filiale;
import backend.model.Prodotto;
import backend.model.Stock;
import backend.repository.ProdottoRepository;
import backend.service.FilialeService;
import backend.service.StockService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AdminProdottoService {
    private final FilialeService filialeService;
    private final ProdottoRepository prodottoRepository;
    private final ProductMapper productMapper;
    private final StockMapper stockMapper;
    private final StockService stockService;

    AdminProdottoService(FilialeService filialeService, ProdottoRepository prodottoRepository, ProductMapper productMapper, StockMapper stockMapper, StockService stockService)
    {
        this.filialeService = filialeService;
        this.prodottoRepository = prodottoRepository;
        this.productMapper = productMapper;
        this.stockMapper = stockMapper;
        this.stockService = stockService;
    }

    public List<ResponseProductDTO> getAll() {
        return prodottoRepository.findAll().stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public ResponseProductDTO createProduct(CreateProductDTO createDTO) {
        // 1. Crea e salva la nuova filiale per ottenere l'ID
        Prodotto nuovoProdotto = productMapper.fromCreateDto(createDTO);
        Prodotto prodottoSalvato = prodottoRepository.save(nuovoProdotto);

        // 2. Recupera tutti i prodotti esistenti
        List<Filiale> filiali = filialeService.getAll();

        // 3. Per ogni prodotto, crea un nuovo oggetto Stock
        List<Stock> nuoviStock = filiali.stream()
                .map(filiale -> stockMapper.createStockFromNewBranchOrProduct(nuovoProdotto, filiale))
                .collect(Collectors.toList());

        // 4. Salva tutti i nuovi stock in un'unica operazione efficiente
        if (!nuoviStock.isEmpty()) {
            stockService.saveAll(nuoviStock);
        }

        // 5. Mappa l'entità salvata nel DTO di risposta e restituiscila
        return productMapper.toDto(prodottoSalvato);

    }

    @Transactional
    public ResponseProductDTO updateProduct(UUID id, UpdateProductDTO updateDTO) {
        Prodotto prodottoEsistente = prodottoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Prodotto non trovato con id: " + id));

        productMapper.partialUpdateFromUpdate(updateDTO, prodottoEsistente);

        Prodotto prodottoAggiornato = prodottoRepository.save(prodottoEsistente);
        return productMapper.toDto(prodottoAggiornato);
    }

    @Transactional
    public Void deleteProduct(UUID id) {
        // 1. Verifica che la filiale esista, altrimenti lancia un'eccezione
        if (!prodottoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Impossibile cancellare: Prodotto non trovato con ID: " + id);
        }

        // 2. Cancella tutti i record di stock associati a quella filiale
        stockService.deleteAllByProdottoId(id);

        // 3. Cancella la filiale stessa
        prodottoRepository.deleteById(id);
        return null;
    }
}
