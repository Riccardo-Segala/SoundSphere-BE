package backend.service;

import backend.model.Ordine;
import backend.repository.OrdineRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OrdineService extends GenericService<Ordine, UUID> {
    public OrdineService(OrdineRepository repository) {
        super(repository); // Passa il repository al costruttore della classe base
    }

}
