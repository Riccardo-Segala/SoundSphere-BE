package backend.service;

import backend.model.Filiale;
import backend.repository.FilialeRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class FilialeService extends GenericService<Filiale, UUID> {
    public FilialeService(FilialeRepository repository) {
        super(repository); // Passa il repository al costruttore della classe base
    }
}
