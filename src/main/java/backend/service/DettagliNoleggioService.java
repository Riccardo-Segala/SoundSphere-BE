package backend.service;

import backend.model.DettagliNoleggio;
import backend.model.embeddable.NoleggioProdottoId;
import backend.repository.DettagliNoleggioRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DettagliNoleggioService extends GenericService<DettagliNoleggio, NoleggioProdottoId> {
    private final DettagliNoleggioRepository dettagliNoleggioRepository;
    public DettagliNoleggioService(DettagliNoleggioRepository dettagliNoleggioRepository) {
        super(dettagliNoleggioRepository);
        this.dettagliNoleggioRepository= dettagliNoleggioRepository;// Passa il repository al costruttore della classe base
    }

    @Transactional
    public void createByList(List<DettagliNoleggio> rentalDetails) {
        dettagliNoleggioRepository.saveAll(rentalDetails);
    }

}
