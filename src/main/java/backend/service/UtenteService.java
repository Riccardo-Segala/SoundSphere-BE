package backend.service;

import backend.model.Utente;
import backend.repository.UtenteRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UtenteService extends GenericService<Utente, UUID> {

    public UtenteService(UtenteRepository repository) {
        super(repository); // Passa il repository al costruttore della classe base
    }


}