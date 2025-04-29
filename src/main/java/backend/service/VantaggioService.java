package backend.service;

import backend.model.Utente;
import backend.model.Vantaggio;
import backend.repository.UtenteRepository;
import backend.repository.VantaggioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class VantaggioService extends GenericService<Vantaggio, UUID> {

    public VantaggioService(VantaggioRepository repository) {
        super(repository); // Passa il repository al costruttore della classe base
    }

}
