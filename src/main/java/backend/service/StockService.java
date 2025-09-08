package backend.service;

import backend.dto.dipendente.ResponseEmployeeDTO;
import backend.dto.filiale.ResponseBranchDTO;
import backend.dto.stock.ResponseStockDTO;
import backend.dto.stock.UpdateStockDTO;
import backend.dto.stock.admin.UpdateStockFromAdminDTO;
import backend.exception.OutOfStockException;
import backend.exception.ResourceNotFoundException;
import backend.mapper.StockMapper;
import backend.model.Filiale;
import backend.model.Stock;
import backend.model.embeddable.FilialeProdottoId;
import backend.repository.StockRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StockService extends GenericService<Stock, FilialeProdottoId> {
    private final StockRepository stockRepository;
    private final FilialeService filialService;
    private final StockMapper stockMapper;
    private final DipendenteService dipendenteService;
    private final ProdottoService prodottoService;


    @Autowired
    public StockService(StockRepository stockRepository, FilialeService filialService, StockMapper stockMapper, DipendenteService dipendenteService, ProdottoService prodottoService) {
        super(stockRepository); // Passa il repository al costruttore della classe base
        this.stockRepository = stockRepository;
        this.filialService = filialService;
        this.stockMapper = stockMapper;
        this.dipendenteService = dipendenteService;
        this.prodottoService = prodottoService;
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


    public void saveAll(List<Stock> nuoviStock) {
        stockRepository.saveAll(nuoviStock);
    }

    public void deleteAllByFilialeId(UUID id) {
        stockRepository.deleteAllByFilialeId(id);
    }

    public void deleteAllByProdottoId(UUID id) {
        stockRepository.deleteAllByProdottoId(id);
    }


    // --- METODI PER IL DIPENDENTE ---

    // Recupera tutto lo stock (anche a quantità 0) per la filiale del dipendente autenticato
    @Transactional
    public List<ResponseStockDTO> getStockForMyFiliale(UUID dipendenteId) {
        // 1. Delega al DipendenteService il compito di trovare la filiale.
        ResponseBranchDTO filialeDTO = dipendenteService.getMyBranch(dipendenteId);
        UUID filialeId = filialeDTO.id();

        // 2. Recupera i dati specifici dello stock.
        List<Stock> stockDellaFiliale = stockRepository.findByFilialeId(filialeId);

        // 3. Delega la mappatura allo StockMapper.
        return stockDellaFiliale.stream()
                .map(stockMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public ResponseStockDTO getStockForSpecificProductInMyFiliale(UUID dipendenteId, UUID prodottoId) {
        // 1. Ottiene la filiale del dipendente tramite il DipendenteService.
        ResponseBranchDTO filialeDTO = dipendenteService.getMyBranch(dipendenteId);
        UUID filialeId = filialeDTO.id();

        // 2. Costruisce l'ID composito per la ricerca.
        FilialeProdottoId stockId = new FilialeProdottoId(filialeId, prodottoId);

        // 3. Cerca la specifica voce di stock nel repository.
        // Se non la trova, lancia un'eccezione chiara.
        Stock stock = stockRepository.findById(stockId)
                .orElseThrow(() -> new ResourceNotFoundException("Il prodotto con ID " + prodottoId + " non è gestito in questa filiale."));

        // 4. Usa lo StockMapper esistente per convertire l'entità nel DTO di risposta.
        return stockMapper.toDto(stock);
    }


    @Transactional
    public ResponseStockDTO updateStockForMyFiliale(UUID dipendenteId, UUID prodottoId, UpdateStockDTO updateDTO) {
        // 1. Usa il DipendenteService per ottenere il DTO del dipendente.
        ResponseEmployeeDTO dipendenteDTO = dipendenteService.getEmployeeDetailsById(dipendenteId);

        // Estrai l'ID della filiale direttamente dal DTO.
        UUID filialeDelDipendenteId = dipendenteDTO.filialeId();

        // 2. Sicurezza e recupero dati: Cerca lo stock usando l'ID della filiale ottenuto dal DTO.
        FilialeProdottoId stockId = new FilialeProdottoId(filialeDelDipendenteId, prodottoId);
        Stock stock = stockRepository.findById(stockId)
                .orElseThrow(() -> new ResourceNotFoundException("Stock non trovato per questo prodotto nella tua filiale."));

        // Regola: La quantità non può essere negativa.
        // Questo è un secondo livello di sicurezza rispetto alla validazione sul DTO.
        if (updateDTO.quantita() != null && updateDTO.quantita() < 0) {
            throw new IllegalArgumentException("La quantità non può essere negativa.");
        }

        // 2. Il mapper aggiorna l'entità esistente con i dati del DTO
        stockMapper.partialUpdateFromUpdate(updateDTO, stock);

        // Il campo 'quantitaPerNoleggio' non viene toccato e salvo
        Stock updatedStock = stockRepository.save(stock);

        // 4. Restituisce il DTO aggiornato.
        return stockMapper.toDto(updatedStock);
    }


    // --- METODI PER L'ADMIN ---

    // Recupera tutte le giacenze di magazzino presenti nel database
    @Transactional
    public List<ResponseStockDTO> getAllStock() {
        return stockRepository.findAll()
                .stream()
                .map(stockMapper::toDto) // Converte ogni entità Stock in un ResponseStockDTO
                .collect(Collectors.toList());
    }

    // Recupera una singola giacenza di magazzino tramite il suo ID composito.
    @Transactional
    public ResponseStockDTO getStockById(FilialeProdottoId id) {
        Stock stock = stockRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Entità Stock non trovata (filiale-prodotto) con id: " + id));

        return stockMapper.toDto(stock);
    }

    @Transactional
    public List<ResponseStockDTO> getAllStockByBranch(UUID filialeId) {
        // 1. Best practice: Verifica prima che la risorsa correlata (la filiale) esista.
        //    Questo fornisce un messaggio di errore 404 più chiaro se l'ID è sbagliato.
        if (!filialService.existsById(filialeId)) { // FilialeService ha un metodo existsById
            throw new ResourceNotFoundException("Nessuna filiale trovata con ID: " + filialeId);
        }

        // 2. Recupera i dati grezzi dal repository.
        List<Stock> stocks = stockRepository.findByFilialeId(filialeId);

        // 3. Converte la lista di entità in una lista di DTO usando lo stream e il mapper.
        //    Se non ci sono stock per una filiale valida, restituirà correttamente una lista vuota.
        return stocks.stream()
                .map(stockMapper::toDto)
                .collect(Collectors.toList());
    }

    //Recupera tutte le voci di stock per un prodotto specifico, cercandolo in tutte le filiali.
    @Transactional
    public List<ResponseStockDTO> getAllStockByProduct(UUID prodottoId) {
        // Se l'ID è invalido, restituisci un errore 404 chiaro.
        if (!prodottoService.existsById(prodottoId)) {
            throw new ResourceNotFoundException("Nessun prodotto trovato con ID: " + prodottoId);
        }

        // 1. Recupera i dati grezzi dal repository.
        List<Stock> stocks = stockRepository.findByProdottoId(prodottoId);

        // 2. Converte la lista di entità in una lista di DTO.
        //    Se il prodotto non è presente in nessuna filiale, restituirà correttamente una lista vuota.
        return stocks.stream()
                .map(stockMapper::toDto)
                .collect(Collectors.toList());
    }

    // Aggiorna una giacenza di magazzino esistente.
    @Transactional
    public ResponseStockDTO updateAdminStock(FilialeProdottoId id, UpdateStockFromAdminDTO updateAdminDTO) {
        Stock stock = stockRepository.findById(id)
                      .orElseThrow(() -> new ResourceNotFoundException("Entità Stock non trovata (filiale-prodotto) con id: " + id));

        // --- BLOCCO DI VALIDAZIONE AGGIORNATO E NULL-SAFE ---
        // Controlla la quantità solo se è stata fornita nel DTO (non è null)
        if (updateAdminDTO.quantita() != null && updateAdminDTO.quantita() < 0) {
            throw new IllegalArgumentException("La quantità non può essere negativa.");
        }

        // Controlla la quantità per noleggio solo se è stata fornita nel DTO (non è null)
        if (updateAdminDTO.quantitaPerNoleggio() != null && updateAdminDTO.quantitaPerNoleggio() < 0) {
            throw new IllegalArgumentException("La quantità per noleggio non può essere negativa.");
        }

        // 2. Il mapper aggiorna l'entità esistente con i dati del DTO
        stockMapper.partialUpdateFromAdminUpdate(updateAdminDTO, stock);

        // 3. Salva l'entità aggiornata nel database
        Stock updatedStock = stockRepository.save(stock);

        // 4. Converte l'entità aggiornata in un DTO da restituire
        return stockMapper.toDto(updatedStock);
    }

}