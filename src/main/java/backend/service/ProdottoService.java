package backend.service;

import backend.dto.prodotto.ResponseProductDTO;
import backend.mapper.ProductMapper;
import backend.model.Prodotto;
import backend.repository.ProdottoRepository;
import backend.repository.RecensioneRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ProdottoService extends GenericService<Prodotto, UUID> {
    private final ProdottoRepository repository;
    private final RecensioneRepository recensioneRepository;
    private final ProductMapper productMapper;

    public ProdottoService(ProdottoRepository repository, RecensioneRepository recensioneRepository, ProductMapper productMapper) {
        super(repository); // Passa il repository al costruttore della classe base
        this.repository = repository;
        this.recensioneRepository = recensioneRepository;
        this.productMapper = productMapper;
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
}
