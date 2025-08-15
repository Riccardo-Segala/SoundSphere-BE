package backend.service;

import backend.model.Filiale;
import backend.model.Prodotto;
import backend.repository.FilialeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class FilialeService extends GenericService<Filiale, UUID> {

    @Autowired
    private FilialeRepository filialeRepository;
    public FilialeService(FilialeRepository repository) {
        super(repository); // Passa il repository al costruttore della classe base
    }

    public List<Prodotto> getProductInStockByBranchId(UUID filialeId) {
        return filialeRepository.getProductInStockByBranchId(filialeId);
    }

}
