package backend.mapper.common;

import backend.dto.common.HasRole;
import backend.model.Ruolo;
import backend.model.Utente;
import backend.model.UtenteRuolo;
import backend.repository.RuoloRepository;
import org.mapstruct.AfterMapping;
import org.mapstruct.MappingTarget;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public interface UserRoleMappingSupport {
    @AfterMapping
    default void linkRolesToUser(HasRole dto, @MappingTarget Utente utente, RuoloRepository ruoloRepository) {
        // Usa il metodo dell'interfaccia
        Set<UUID> ruoliIds = dto.ruoliIds();

        if (CollectionUtils.isEmpty(ruoliIds)) {
            if(utente.getUtenteRuoli() != null) {
                utente.getUtenteRuoli().clear();
            }
            return;
        }

        List<Ruolo> ruoliTrovati = ruoloRepository.findAllById(ruoliIds);

        Set<UtenteRuolo> newAssignments = ruoliTrovati.stream()
                .map(ruolo -> {
                    UtenteRuolo assignment = new UtenteRuolo();
                    assignment.setUtente(utente);

                    assignment.setRuolo(ruolo);
                    return assignment;
                })
                .collect(Collectors.toSet());

        if(utente.getUtenteRuoli() != null) {
            utente.getUtenteRuoli().clear();
            utente.getUtenteRuoli().addAll(newAssignments);
        } else {
            utente.setUtenteRuoli(newAssignments);
        }
    }
}
