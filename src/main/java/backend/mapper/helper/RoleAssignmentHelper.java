package backend.mapper.helper;

import backend.mapper.resolver.RoleResolver;
import backend.model.Ruolo;
import backend.model.Utente;
import backend.service.UtenteRuoloService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class RoleAssignmentHelper {

    private final RoleResolver roleResolver;
    private final UtenteRuoloService utenteRuoloService;

    public void synchronizeRoles(Utente utente, Set<UUID> ruoliIds) {
        if (utente == null) {
            throw new IllegalArgumentException("L'utente non può essere nullo.");
        }

        Set<Ruolo> nuoviRuoli = roleResolver.findRolesByIds(ruoliIds);

        if (nuoviRuoli.size() != ruoliIds.size()) {
            throw new RuntimeException("Uno o più ruoli non trovati");
        }

        utenteRuoloService.handleRoleTransition(List.of(utente), List.copyOf(nuoviRuoli));
    }
}