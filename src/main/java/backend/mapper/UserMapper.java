package backend.mapper;
import backend.dto.utente.CreateUserDTO;
import backend.dto.utente.ResponseUserDTO;
import backend.dto.utente.UpdateUserDTO;
import backend.dto.utente.admin.CreateUserFromAdminDTO;
import backend.dto.utente.admin.UpdateUserFromAdminDTO;
import backend.mapper.helper.RoleAssignmentHelper;
import backend.mapper.resolver.RoleResolver;
import backend.model.Utente;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {BenefitMapper.class, RoleMapper.class, RoleResolver.class})
public abstract class UserMapper implements GenericMapper<Utente, CreateUserDTO, UpdateUserDTO, ResponseUserDTO>{

    @Autowired
    protected RoleAssignmentHelper roleAssignmentHelper;

    @Override
    @Mapping(
            source = "utenteRuoli",
            target = "ruoli",
            qualifiedByName = "mapUtenteRuoliToRuoliStrings"
    )
    public abstract ResponseUserDTO toDto(Utente utente);

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract Utente partialUpdateFromCreate(CreateUserDTO createUserDto, @MappingTarget Utente utente);

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract Utente partialUpdateFromUpdate(UpdateUserDTO updateUserDto, @MappingTarget Utente utente);
    @Mapping(target = "utenteRuoli", ignore = true)
    @Mapping(target = "password", ignore = true)
    public abstract Utente fromAdminCreateDto(CreateUserFromAdminDTO createDto);
    @Mapping(target = "utenteRuoli", ignore = true)
    @Mapping(target = "password", ignore = true)
    public abstract Utente fromAdminUpdateDto(UpdateUserFromAdminDTO updateDto);

    @Mapping(target = "utenteRuoli", ignore = true)
    @Mapping(target = "password", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract Utente partialUpdateFromAdminCreate(CreateUserFromAdminDTO createDto, @MappingTarget Utente utente);
    @Mapping(target = "utenteRuoli", ignore = true)
    @Mapping(target = "password", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract Utente partialUpdateFromAdminUpdate(UpdateUserFromAdminDTO updateDto, @MappingTarget Utente utente);

    @AfterMapping
    protected void linkRolesToUserForUpdate(UpdateUserFromAdminDTO dto, @MappingTarget Utente utente) {
        roleAssignmentHelper.synchronizeRoles(utente, dto.ruoliIds());
    }
}