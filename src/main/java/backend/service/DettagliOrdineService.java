package backend.service;

import backend.model.DettagliOrdine;
import backend.model.embeddable.OrdineProdottoId;
import backend.repository.DettagliOrdineRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DettagliOrdineService extends GenericService<DettagliOrdine, OrdineProdottoId> {
    private final DettagliOrdineRepository dettagliOrdineRepository;
    public DettagliOrdineService(DettagliOrdineRepository repository, DettagliOrdineRepository dettagliOrdineRepository) {
        super(repository);
        this.dettagliOrdineRepository = dettagliOrdineRepository;
    }

    @Transactional
    public void createByList(List<DettagliOrdine> orderDetails) {
        dettagliOrdineRepository.saveAll(orderDetails);
    }
}
