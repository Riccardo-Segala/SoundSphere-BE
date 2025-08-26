package backend.mapper;

import backend.dto.indirizzo_utente.CreateUserAddressDTO;
import backend.dto.indirizzo_utente.UpdateUserAddressDTO;
import backend.dto.indirizzo_utente.ResponseUserAddressDTO;
import backend.model.IndirizzoUtente;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserAddressMapper extends GenericMapper<IndirizzoUtente, CreateUserAddressDTO, UpdateUserAddressDTO, ResponseUserAddressDTO> {


    @Override
    IndirizzoUtente fromCreateDto(CreateUserAddressDTO createDTO);

    @Override
    @InheritConfiguration(name = "fromCreateDto")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    IndirizzoUtente partialUpdateFromCreate(CreateUserAddressDTO createUserAddressDTO, @MappingTarget IndirizzoUtente indirizzoUtente);

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    IndirizzoUtente partialUpdateFromUpdate(UpdateUserAddressDTO updateUserAddressDTO, @MappingTarget IndirizzoUtente indirizzoUtente);
}