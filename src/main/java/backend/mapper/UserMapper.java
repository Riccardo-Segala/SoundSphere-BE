package backend.mapper;
import backend.dto.utente.CreateUserDTO;
import backend.dto.utente.ResponseUserDTO;
import backend.dto.utente.RoleAssignmentDTO;
import backend.dto.utente.UpdateUserDTO;
import backend.dto.utente.admin.CreateUserFromAdminDTO;
import backend.dto.utente.admin.UpdateUserFromAdminDTO;
import backend.mapper.common.UserRoleMappingSupport;
import backend.mapper.resolver.RoleResolver;
import backend.model.Ruolo;
import backend.model.Utente;
import backend.model.UtenteRuolo;
import backend.repository.RuoloRepository;
import org.mapstruct.*;
import java.util.Optional;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {BenefitMapper.class, RoleMapper.class, RoleResolver.class, RuoloRepository.class})
public interface UserMapper extends GenericMapper<Utente, CreateUserDTO, UpdateUserDTO, ResponseUserDTO>, UserRoleMappingSupport {

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
}