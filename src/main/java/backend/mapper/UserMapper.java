package backend.mapper;

import backend.dto.utente.CreateUserDTO;
import backend.dto.utente.ResponseUserDTO;
import backend.dto.utente.UpdateUserDTO;
import backend.model.Utente;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {BenefitMapper.class})
public interface UserMapper extends GenericMapper<Utente, CreateUserDTO, UpdateUserDTO, ResponseUserDTO> {

    @Override
    ResponseUserDTO toDto(Utente utente);

    @Override
    @Mapping(source = "vantaggioId", target = "vantaggio.id")
    Utente fromCreateDto(CreateUserDTO createUserDto);

    @Override
    Utente fromUpdateDto(UpdateUserDTO updateUserDto);

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "vantaggioId", target = "vantaggio.id")
    Utente partialUpdateFromCreate(CreateUserDTO createUserDto, @MappingTarget Utente utente);

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Utente partialUpdateFromUpdate(UpdateUserDTO updateUserDto, @MappingTarget Utente utente);
}