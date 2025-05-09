package backend.service;

import backend.model.IndirizzoUtente;
import backend.repository.IndirizzoUtenteRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class IndirizzoUtenteService extends GenericService<IndirizzoUtente, UUID> {
    public IndirizzoUtenteService(IndirizzoUtenteRepository repository) {
        super(repository); // Passa il repository al costruttore della classe base
    }
}
