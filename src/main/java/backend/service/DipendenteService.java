package backend.service;

import backend.model.Dipendente;
import backend.repository.DipendenteRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DipendenteService extends GenericService<Dipendente, UUID> {
    public DipendenteService(DipendenteRepository repository) {
        super(repository); // Passa il repository al costruttore della classe base
    }

}
