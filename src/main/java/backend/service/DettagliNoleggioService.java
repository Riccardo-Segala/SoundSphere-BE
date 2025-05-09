package backend.service;

import backend.model.DettagliNoleggio;
import backend.model.embeddable.NoleggioProdottoId;
import backend.model.embeddable.UtenteProdottoId;
import backend.repository.DettagliNoleggioRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DettagliNoleggioService extends GenericService<DettagliNoleggio, NoleggioProdottoId> {
    public DettagliNoleggioService(DettagliNoleggioRepository repository) {
        super(repository); // Passa il repository al costruttore della classe base
    }

}
