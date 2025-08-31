package backend.service;

import backend.model.Ruolo;
import backend.repository.RuoloRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RuoloService {
    private final RuoloRepository ruoloRepository;

    public Ruolo findByName(String roleName) {
        String roleNameUpperCase = roleName.toUpperCase();
        return ruoloRepository.findByNome(roleNameUpperCase)
                .orElseThrow(() -> new IllegalStateException("Ruolo '" + roleNameUpperCase + "' non trovato"));
    }
}
