package backend.service;

import backend.model.Utente;
import backend.model.Vantaggio;
import backend.repository.UtenteRepository;
import backend.repository.VantaggioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class VantaggioService extends GenericService<Vantaggio, UUID> {
    @Autowired
    private UtenteRepository utenteRepository;
    @Autowired
    private VantaggioRepository vantaggioRepository;

    public VantaggioService(VantaggioRepository repository) {
        super(repository); // Passa il repository al costruttore della classe base
    }

    //Calcola l'importo dello sconto in base ai punti dell'utente.
    public double calcolaSconto(UUID utenteId) {
        Optional<Utente> utenteOptional = utenteRepository.findById(utenteId);
        if (utenteOptional.isEmpty() || utenteOptional.get().getPunti() == null) {
            return 0.0;
        }

        Integer puntiUtente = utenteOptional.get().getPunti();

        Optional<Vantaggio> vantaggioOptional = vantaggioRepository.findVantaggioByPunteggio(puntiUtente);

        if (vantaggioOptional.isEmpty()) {
            return 0.0;
        }
        return vantaggioOptional.get().getSconto();
    }

    public Vantaggio findVantaggioByPunti(int nuovoPunteggio) {
        return vantaggioRepository.findVantaggioByPunteggio(nuovoPunteggio)
                .orElseThrow(() -> new RuntimeException("Nessun vantaggio trovato per il punteggio: " + nuovoPunteggio));
    }
}
