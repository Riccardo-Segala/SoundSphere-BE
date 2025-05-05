package backend.service;

import backend.model.Prodotto;
import backend.repository.ProdottoRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProdottoService extends GenericService<Prodotto, UUID> {
    public ProdottoService(ProdottoRepository repository) {
        super(repository); // Passa il repository al costruttore della classe base
    }
}
