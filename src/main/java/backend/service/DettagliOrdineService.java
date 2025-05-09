package backend.service;

import backend.model.DettagliOrdine;
import backend.model.embeddable.OrdineProdottoId;
import backend.repository.DettagliOrdineRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DettagliOrdineService extends GenericService<DettagliOrdine, OrdineProdottoId> {
    public DettagliOrdineService(DettagliOrdineRepository repository) {
        super(repository); // Passa il repository al costruttore della classe base
    }
}
