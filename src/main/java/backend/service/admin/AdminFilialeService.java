package backend.service.admin;

import backend.dto.filiale.CreateBranchDTO;
import backend.dto.filiale.ResponseBranchDTO;
import backend.dto.filiale.UpdateBranchDTO;
import backend.exception.ResourceNotFoundException;
import backend.mapper.BranchMapper;
import backend.mapper.StockMapper;
import backend.model.Filiale;
import backend.model.Prodotto;
import backend.model.Stock;
import backend.repository.FilialeRepository;
import backend.service.ProdottoService;
import backend.service.StockService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AdminFilialeService {
    private final FilialeRepository filialeRepository;
    private final BranchMapper branchMapper;
    private final ProdottoService prodottoService;
    private final StockService stockService;
    private final StockMapper stockMapper;

    public AdminFilialeService(FilialeRepository filialeRepository, BranchMapper branchMapper, ProdottoService prodottoService, StockService stockService, StockMapper stockMapper) {
        this.filialeRepository = filialeRepository;
        this.branchMapper = branchMapper;
        this.prodottoService = prodottoService;
        this.stockService = stockService;
        this.stockMapper = stockMapper;
    }

    public List<ResponseBranchDTO> getAll() {
        return filialeRepository.findAll().stream()
                .map(branchMapper::toDto)
                .collect(Collectors.toList());
    }

    public ResponseBranchDTO getById(UUID id) {
        return filialeRepository.findById(id)
                .map(branchMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Filiale non trovata"));
    }

    @Transactional
    public ResponseBranchDTO createBranch(CreateBranchDTO createDTO) {
        // 1. Crea e salva la nuova filiale per ottenere l'ID
        Filiale nuovaFiliale = branchMapper.fromCreateDto(createDTO);
        Filiale filialeSalvata = filialeRepository.save(nuovaFiliale);

        // 2. Recupera tutti i prodotti esistenti
        List<Prodotto> prodotti = prodottoService.getAll();

        // 3. Per ogni prodotto, crea un nuovo oggetto Stock
        List<Stock> nuoviStock = prodotti.stream()
                .map(prodotto -> stockMapper.createStockFromNewBranchOrProduct(prodotto, nuovaFiliale))
                .collect(Collectors.toList());

        // 4. Salva tutti i nuovi stock in un'unica operazione efficiente
        if (!nuoviStock.isEmpty()) {
            stockService.saveAll(nuoviStock);
        }

        // 5. Mappa l'entità salvata nel DTO di risposta e restituiscila
        return branchMapper.toDto(filialeSalvata);

    }

    @Transactional
    public ResponseBranchDTO updateBranch(UUID id, UpdateBranchDTO updateDTO) {
        Filiale filialeEsistente = filialeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Filiale non trovata con id: " + id));

        branchMapper.partialUpdateFromUpdate(updateDTO, filialeEsistente);

        Filiale filialeAggiornata = filialeRepository.save(filialeEsistente);
        return branchMapper.toDto(filialeAggiornata);
    }

    @Transactional
    public Void deleteBranch(UUID id) {
        // 1. Verifica che la filiale esista, altrimenti lancia un'eccezione
        if (!filialeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Impossibile cancellare: filiale non trovata con ID: " + id);
        }

        // 2. Cancella tutti i record di stock associati a quella filiale
        stockService.deleteAllByFilialeId(id);

        // 3. Cancella la filiale stessa
        filialeRepository.deleteById(id);
        return null;
    }
}
