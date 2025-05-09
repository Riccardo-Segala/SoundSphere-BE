package backend.service;

import backend.model.Carrello;
import backend.repository.CarrelloRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CarrelloService extends GenericService<Carrello, UUID> {
    public CarrelloService(CarrelloRepository repository) {
        super(repository); // Passa il repository al costruttore della classe base
    }

}
