package backend.service;

import backend.model.Carrello;
import backend.model.embeddable.UtenteProdottoId;
import backend.repository.CarrelloRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CarrelloService extends GenericService<Carrello, UtenteProdottoId> {
    public CarrelloService(CarrelloRepository repository) {
        super(repository); // Passa il repository al costruttore della classe base
    }

}
