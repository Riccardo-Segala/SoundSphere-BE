package backend.mapper.resolver;

import backend.exception.ResourceNotFoundException;
import backend.model.Ruolo;
import backend.repository.RuoloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RoleResolver {

    @Autowired
    private RuoloRepository ruoloRepository;
    public Ruolo roleFromRoleId(UUID roleId) {
        if (roleId == null) {
            return null;
        }
        return ruoloRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Ruolo non trovato con ID: " + roleId));
    }
}
