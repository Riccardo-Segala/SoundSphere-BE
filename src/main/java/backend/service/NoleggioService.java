package backend.service;

import backend.model.Noleggio;
import backend.repository.NoleggioRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class NoleggioService extends GenericService<Noleggio, UUID> {
    public NoleggioService(NoleggioRepository repository) {
        super(repository); // Passa il repository al costruttore della classe base
    }
}
