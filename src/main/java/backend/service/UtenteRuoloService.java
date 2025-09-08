package backend.service;

import backend.dto.dati_statici.ResponseStaticDataDTO;
import backend.model.Ruolo;
import backend.model.Utente;
import backend.model.UtenteRuolo;
import backend.repository.UtenteRuoloRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service

public class UtenteRuoloService {
    private final UtenteRuoloRepository repository;
    private final DatiStaticiService datiStaticiService;


    private String renewalDurationName;

    public UtenteRuoloService(UtenteRuoloRepository repository,
                              DatiStaticiService datiStaticiService,
                              @Value("${app.static-data.renewal-duration}")String renewalDurationName) {
        this.repository = repository;
        this.datiStaticiService = datiStaticiService;
        this.renewalDurationName = renewalDurationName;
    }

    public LocalDate getExpirationDateForEventManagerRole(LocalDate currentExpirationDate) {

        ResponseStaticDataDTO renewalDuration = datiStaticiService.getStaticDataByName(renewalDurationName);
        int monthsToAdd = (int)renewalDuration.valore();

        if (currentExpirationDate != null) {
            // Se c'è già una data di scadenza, la estende
            return currentExpirationDate.plusMonths(monthsToAdd);
        } else {
            // Se non c'è una data, ne crea una nuova partendo da oggi (nuova assegnazione)
            return LocalDate.now().plusMonths(monthsToAdd);
        }
    }

    @Transactional
    public void handleRoleTransition(List<Utente> users, List<Ruolo> desiredRoles) {
        if (CollectionUtils.isEmpty(users)) {
            return;
        }

        // --- FASE 1: PREPARAZIONE E CARICAMENTO MASSIVO ---

        Set<Ruolo> desiredRolesSet = desiredRoles == null ? Collections.emptySet() : new HashSet<>(desiredRoles);

        Map<UUID, List<UtenteRuolo>> allCurrentUserRolesMap = repository.findByUtenteIn(users)
                .stream()
                .collect(Collectors.groupingBy(ur -> ur.getUtente().getId()));

        List<UtenteRuolo> finalAssignmentsToSave = new ArrayList<>();
        List<UtenteRuolo> finalAssignmentsToRemove = new ArrayList<>();

        // --- FASE 2: CALCOLO DELLE MODIFICHE (AGGIUNGI, RIMUOVI, AGGIORNA) ---

        for (Utente user : users) {
            List<UtenteRuolo> currentAssignments = allCurrentUserRolesMap.getOrDefault(user.getId(), Collections.emptyList());
            Set<Ruolo> currentRolesSet = currentAssignments.stream()
                    .map(UtenteRuolo::getRuolo)
                    .collect(Collectors.toSet());

            // A. Calcola i ruoli da AGGIUNGERE: (ruoli desiderati) - (ruoli attuali)
            Set<Ruolo> rolesToAdd = new HashSet<>(desiredRolesSet);
            rolesToAdd.removeAll(currentRolesSet);

            // B. Calcola i ruoli da RIMUOVERE: (ruoli attuali) - (ruoli desiderati)
            Set<Ruolo> rolesToRemove = new HashSet<>(currentRolesSet);
            rolesToRemove.removeAll(desiredRolesSet);

            // C. Calcola i ruoli da MANTENERE/AGGIORNARE: (ruoli attuali) INTERSEZIONE (ruoli desiderati)
            Set<Ruolo> rolesToKeep = new HashSet<>(currentRolesSet);
            rolesToKeep.retainAll(desiredRolesSet);

            // --- Applica le modifiche calcolate ---

            // Prepara le RIMOZIONI
            if (!rolesToRemove.isEmpty()) {
                // Filtra le assegnazioni da rimuovere
                List<UtenteRuolo> assignmentsToRemoveForUser = currentAssignments.stream()
                        .filter(ur -> rolesToRemove.contains(ur.getRuolo()))
                        .peek(assignmentToRemove -> {
                            // AZIONE (SIDE-EFFECT): Rimuove l'assegnazione dall'oggetto user in memoria
                            user.getUtenteRuoli().remove(assignmentToRemove);
                        })
                        .collect(Collectors.toList());

                // Aggiunge la lista a quella finale per la cancellazione dal DB
                finalAssignmentsToRemove.addAll(assignmentsToRemoveForUser);
            }

            // Prepara le AGGIUNTE
            if (!rolesToAdd.isEmpty()) {
                // 1. Usiamo lo stream per creare la lista di nuove assegnazioni
                List<UtenteRuolo> newAssignments = rolesToAdd.stream()
                        .map(role -> {
                            // FASE A (TRASFORMAZIONE): da Ruolo a UtenteRuolo
                            LocalDate expirationDate = role.getNome().equals("ORGANIZZATORE_EVENTI")
                                    ? getExpirationDateForEventManagerRole(null)
                                    : null;
                            return new UtenteRuolo(user, role, expirationDate);
                        })
                        .peek(newAssignment -> {
                            // FASE B (AZIONE/SIDE-EFFECT): aggiorna la collezione dell'utente
                            user.getUtenteRuoli().add(newAssignment);
                        })
                        .collect(Collectors.toList());

                // 2. Aggiungiamo la lista appena creata a quella finale da salvare
                finalAssignmentsToSave.addAll(newAssignments);
            }

            // --- NUOVA SEZIONE: GESTIONE AGGIORNAMENTI ---
            // Controlla i ruoli mantenuti per vedere se necessitano di un aggiornamento.
            if (!rolesToKeep.isEmpty()) {
                currentAssignments.stream()
                        .filter(ur -> rolesToKeep.contains(ur.getRuolo())) // Filtra per i soli ruoli mantenuti
                        .forEach(existingAssignment -> {
                            // Applica la logica di aggiornamento solo se il ruolo è ORGANIZZATORE_EVENTI
                            if (existingAssignment.getRuolo().getNome().equals("ORGANIZZATORE_EVENTI")) {
                                existingAssignment.setDataScadenza(
                                        getExpirationDateForEventManagerRole(existingAssignment.getDataScadenza())
                                );
                                // Aggiungi l'assegnazione MODIFICATA alla lista da salvare
                                finalAssignmentsToSave.add(existingAssignment);
                            }
                        });
            }
        }

        // --- FASE 3: SCRITTURA ATOMICA SUL DATABASE ---

        if (!finalAssignmentsToRemove.isEmpty()) {
            repository.deleteAllInBatch(finalAssignmentsToRemove);
        }
        if (!finalAssignmentsToSave.isEmpty()) {
            repository.saveAll(finalAssignmentsToSave);
        }
    }
}

