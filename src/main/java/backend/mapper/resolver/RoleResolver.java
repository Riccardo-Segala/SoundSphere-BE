package backend.mapper.resolver;
import backend.model.Ruolo;
import backend.model.UtenteRuolo;
import backend.repository.RuoloRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RoleResolver {


    private final RuoloRepository ruoloRepository;
    @Named("findRolesByIds")
    public Set<Ruolo> findRolesByIds(Set<UUID> ruoliIds) {
        return new HashSet<>(ruoloRepository.findAllById(ruoliIds));
    }

    @Named("mapUtenteRuoliToRuoliStrings")
    public Set<String> mapUtenteRuoliToRuoliStrings(Set<UtenteRuolo> utenteRuoli) {
        if (CollectionUtils.isEmpty(utenteRuoli)) {
            return Collections.emptySet();
        }

        return utenteRuoli.stream()
                .map(UtenteRuolo::getRuolo)
                .map(Ruolo::getNome)
                .collect(Collectors.toSet());
    }
}
