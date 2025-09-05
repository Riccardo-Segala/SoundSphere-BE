package backend.mapper.helper;

import backend.mapper.resolver.RoleResolver;
import backend.model.Ruolo;
import backend.model.Utente;
import backend.model.UtenteRuolo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RoleAssignmentHelper {

    private final RoleResolver roleResolver;

    public void synchronizeRoles(Utente utente, Set<UUID> ruoliIds) {
        if (ruoliIds == null || ruoliIds.isEmpty()) {
            if (utente.getUtenteRuoli() != null) {
                utente.getUtenteRuoli().clear();
            }
            return;
        }

        Set<Ruolo> nuoviRuoli = roleResolver.findRolesByIds(ruoliIds);

        if (nuoviRuoli.size() != ruoliIds.size()) {
            throw new RuntimeException("Uno o più ruoli non trovati");
        }

        Map<UUID, UtenteRuolo> ruoliEsistentiMap = utente.getUtenteRuoli().stream()
                .collect(Collectors.toMap(ur -> ur.getRuolo().getId(), ur -> ur));

        utente.getUtenteRuoli().removeIf(utenteRuolo ->
                !ruoliIds.contains(utenteRuolo.getRuolo().getId())
        );

        for (Ruolo nuovoRuolo : nuoviRuoli) {
            if (!ruoliEsistentiMap.containsKey(nuovoRuolo.getId())) {
                LocalDate dataScadenza = null;

                if ("ORGANIZZATORE_EVENTI".equals(nuovoRuolo.getNome())) {
                    dataScadenza = LocalDate.now().plusMonths(6);
                }

                UtenteRuolo nuovaRelazione = new UtenteRuolo(utente, nuovoRuolo, dataScadenza);
                utente.getUtenteRuoli().add(nuovaRelazione);
            }
        }
    }
}