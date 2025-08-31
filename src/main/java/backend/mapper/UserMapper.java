package backend.mapper;

import backend.dto.utente.CreateUserDTO;
import backend.dto.utente.ResponseUserDTO;
import backend.dto.utente.UpdateUserDTO;
import backend.dto.utente.admin.CreateUserFromAdminDTO;
import backend.dto.utente.admin.UpdateUserFromAdminDTO;
import backend.mapper.resolver.RoleResolver;
import backend.model.Utente;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {BenefitMapper.class, RoleMapper.class, RoleResolver.class})
public interface UserMapper extends GenericMapper<Utente, CreateUserDTO, UpdateUserDTO, ResponseUserDTO> {

    @Override
    ResponseUserDTO toDto(Utente utente);

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Utente partialUpdateFromCreate(CreateUserDTO createUserDto, @MappingTarget Utente utente);

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Utente partialUpdateFromUpdate(UpdateUserDTO updateUserDto, @MappingTarget Utente utente);
    @Mapping(target = "ruoli", source = "ruoliIds")
    Utente fromAdminCreateDto(CreateUserFromAdminDTO createDto);
    @Mapping(target = "ruoli", source = "ruoliIds")
    Utente fromAdminUpdateDto(UpdateUserFromAdminDTO updateDto);

    @Mapping(target = "ruoli", source = "ruoliIds")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Utente partialUpdateFromAdminCreate(CreateUserFromAdminDTO createDto, @MappingTarget Utente utente);
    @Mapping(target = "ruoli", source = "ruoliIds")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Utente partialUpdateFromAdminUpdate(UpdateUserFromAdminDTO updateDto, @MappingTarget Utente utente);
}