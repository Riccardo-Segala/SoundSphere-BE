package backend.mapper.resolver;

import backend.exception.ResourceNotFoundException;
import backend.model.Filiale;
import backend.repository.FilialeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class BranchResolver {

    @Autowired
    private final FilialeRepository filialeRepository;

    public Filiale fromIdToEntity(UUID filialeId) {
        if (filialeId == null) {
            return null;
        }
        return filialeRepository.findById(filialeId)
                .orElseThrow(() -> new ResourceNotFoundException("Filiale non trovata con id: " + filialeId));
    }
}