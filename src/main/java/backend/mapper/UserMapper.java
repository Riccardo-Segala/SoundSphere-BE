package backend.mapper;

import backend.dto.utente.CreateUserDTO;
import backend.dto.utente.ResponseUserDTO;
import backend.dto.utente.UpdateUserDTO;
import backend.model.Utente;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {BenefitMapper.class})
public interface UserMapper {
    @Mapping(source = "vantaggioId", target = "vantaggio.id")
    Utente toEntity(CreateUserDTO createUserDto);

    @Mapping(source = "vantaggio.id", target = "vantaggioId")
    CreateUserDTO toDto(Utente utente);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "vantaggioId", target = "vantaggio.id")
    Utente partialUpdate(CreateUserDTO createUserDto, @MappingTarget Utente utente);

    Utente toEntity(UpdateUserDTO updateUserDto);

    UpdateUserDTO toDto1(Utente utente);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Utente partialUpdate(UpdateUserDTO updateUserDto, @MappingTarget Utente utente);

    Utente toEntity(ResponseUserDTO responseUserDto);

    ResponseUserDTO toDto2(Utente utente);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Utente partialUpdate(ResponseUserDTO responseUserDto, @MappingTarget Utente utente);
}