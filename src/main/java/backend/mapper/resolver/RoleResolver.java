package backend.mapper.resolver;

import backend.model.Ruolo;
import backend.repository.RuoloRepository;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Component
public class RoleResolver {

    @Autowired
    private RuoloRepository ruoloRepository;
    @Named("findRolesByIds")
    public Set<Ruolo> findRolesByIds(Set<UUID> ruoliIds) {
        return new HashSet<>(ruoloRepository.findAllById(ruoliIds));
    }
}
