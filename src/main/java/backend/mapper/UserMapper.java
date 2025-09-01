package backend.mapper;

import backend.dto.common.HasRole;
import backend.dto.utente.CreateUserDTO;
import backend.dto.utente.ResponseUserDTO;
import backend.dto.utente.RoleAssignmentDTO;
import backend.dto.utente.UpdateUserDTO;
import backend.dto.utente.admin.CreateUserFromAdminDTO;
import backend.dto.utente.admin.UpdateUserFromAdminDTO;
import backend.mapper.resolver.RoleResolver;
import backend.model.Ruolo;
import backend.model.Utente;
import backend.model.UtenteRuolo;
import backend.repository.RuoloRepository;
import org.mapstruct.*;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {BenefitMapper.class, RoleMapper.class, RoleResolver.class, RuoloRepository.class})
public interface UserMapper extends GenericMapper<Utente, CreateUserDTO, UpdateUserDTO, ResponseUserDTO> {

    @Override
    @Mapping(
            source = "utenteRuoli",
            target = "ruoli",
            qualifiedByName = "mapUtenteRuoliToRuoliStrings"
    )
    ResponseUserDTO toDto(Utente utente);

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Utente partialUpdateFromCreate(CreateUserDTO createUserDto, @MappingTarget Utente utente);

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Utente partialUpdateFromUpdate(UpdateUserDTO updateUserDto, @MappingTarget Utente utente);
    @Mapping(target = "utenteRuoli", ignore = true)
    @Mapping(target = "password", ignore = true)
    Utente fromAdminCreateDto(CreateUserFromAdminDTO createDto);
    @Mapping(target = "utenteRuoli", ignore = true)
    @Mapping(target = "password", ignore = true)
    Utente fromAdminUpdateDto(UpdateUserFromAdminDTO updateDto);

    @Mapping(target = "utenteRuoli", ignore = true)
    @Mapping(target = "password", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Utente partialUpdateFromAdminCreate(CreateUserFromAdminDTO createDto, @MappingTarget Utente utente);
    @Mapping(target = "utenteRuoli", ignore = true)
    @Mapping(target = "password", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Utente partialUpdateFromAdminUpdate(UpdateUserFromAdminDTO updateDto, @MappingTarget Utente utente);

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

    default void updateRoleFromDto(RoleAssignmentDTO dto, Ruolo ruolo, @MappingTarget Utente utente) {
        if (dto == null || ruolo == null || utente == null) {
            return;
        }

        Optional<UtenteRuolo> existingAssignment = utente.getUtenteRuoli().stream()
                .filter(ur -> ur.getRuolo().equals(ruolo))
                .findFirst();

        if (existingAssignment.isPresent()) {
            // Se esiste, aggiorna solo la data di scadenza sull'oggetto esistente
            existingAssignment.get().setDataScadenza(dto.expirationDate());
        } else {
            // Se non esiste, crea una nuova assegnazione
            UtenteRuolo newAssignment = new UtenteRuolo();
            newAssignment.setUtente(utente);
            newAssignment.setRuolo(ruolo);
            newAssignment.setDataScadenza(dto.expirationDate());

            utente.getUtenteRuoli().add(newAssignment);
        }
    }
}