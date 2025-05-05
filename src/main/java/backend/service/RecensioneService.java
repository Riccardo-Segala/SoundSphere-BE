package backend.service;

import backend.model.Recensione;
import backend.repository.RecensioneRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RecensioneService extends GenericService<Recensione, UUID> {
    public RecensioneService(RecensioneRepository repository) {
        super(repository); // Passa il repository al costruttore della classe base
    }

}
