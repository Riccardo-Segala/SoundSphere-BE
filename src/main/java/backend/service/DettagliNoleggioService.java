package backend.service;

import backend.model.DettagliNoleggio;
import backend.repository.DettagliNoleggioRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DettagliNoleggioService extends GenericService<DettagliNoleggio, UUID> {
    public DettagliNoleggioService(DettagliNoleggioRepository repository) {
        super(repository); // Passa il repository al costruttore della classe base
    }

}
