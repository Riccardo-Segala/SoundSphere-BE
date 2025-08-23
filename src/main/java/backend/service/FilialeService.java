package backend.service;

import backend.model.Filiale;
import backend.model.Prodotto;
import backend.repository.FilialeRepository;
import jakarta.persistence.EntityNotFoundException;
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


    public Filiale getByName(String nome) {
        return filialeRepository.findByName(nome)
                .orElseThrow(() -> new EntityNotFoundException("Filiale non trovata nel database con nome: " + nome));
    }

}
