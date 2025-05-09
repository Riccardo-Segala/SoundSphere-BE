package backend.service;

import backend.model.OrganizzatoreEventi;
import backend.repository.OrganizzatoreEventiRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OrganizzatoreEventiService extends GenericService<OrganizzatoreEventi, UUID> {

    public OrganizzatoreEventiService(OrganizzatoreEventiRepository repository) {
        super(repository); // Passa il repository al costruttore della classe base
    }
}
