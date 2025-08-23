package backend.service;

import backend.model.Prodotto;
import backend.repository.ProdottoRepository;
import backend.repository.RecensioneRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProdottoService extends GenericService<Prodotto, UUID> {
    private final ProdottoRepository repository;
    private final RecensioneRepository recensioneRepository;

    public ProdottoService(ProdottoRepository repository, RecensioneRepository recensioneRepository) {
        super(repository); // Passa il repository al costruttore della classe base
        this.repository = repository;
        this.recensioneRepository = recensioneRepository;
    }

    public Double getAverageStars(UUID prodottoId) {
        // Verifica prima che il prodotto esista, per restituire un 404 se l'ID è invalido
        if (!repository.existsById(prodottoId)) {
            throw new EntityNotFoundException("Prodotto non trovato con ID: " + prodottoId);
        }
        // Chiama il metodo del repository delle recensioni
        return recensioneRepository.findAverageStarsByProdottoId(prodottoId);
    }
}
