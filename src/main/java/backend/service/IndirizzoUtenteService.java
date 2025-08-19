package backend.service;

import backend.model.IndirizzoUtente;
import backend.repository.IndirizzoUtenteRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class IndirizzoUtenteService extends GenericService<IndirizzoUtente, UUID> {
    private final IndirizzoUtenteRepository indirizzoUtenteRepository;
    public IndirizzoUtenteService(IndirizzoUtenteRepository repository, IndirizzoUtenteRepository indirizzoUtenteRepository) {
        super(repository); // Passa il repository al costruttore della classe base
        this.indirizzoUtenteRepository = indirizzoUtenteRepository;
    }

    public IndirizzoUtente findByIdAndValidateOwnership(UUID indirizzoId, UUID utenteId) {
        IndirizzoUtente indirizzo = indirizzoUtenteRepository.findById(indirizzoId)
                .orElseThrow(() -> new EntityNotFoundException("Indirizzo non trovato"));

        if (!indirizzo.getUtente().getId().equals(utenteId)) {
            throw new SecurityException("L'indirizzo non appartiene all'utente corrente.");
        }
        return indirizzo;
    }
}
