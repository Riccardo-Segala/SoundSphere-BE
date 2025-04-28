package backend.mapper;

import backend.dto.indirizzo_utente.CreateUserAddressDTO;
import backend.dto.indirizzo_utente.UpdateUserAddressDTO;
import backend.model.IndirizzoUtente;
import backend.dto.indirizzo_utente.ResponseUserAddressDTO;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserAddressMapper {
    @Mapping(source = "utenteCognome", target = "utente.cognome")
    @Mapping(source = "utenteNome", target = "utente.nome")
    @Mapping(source = "utenteId", target = "utente.id")
    IndirizzoUtente toEntity(CreateUserAddressDTO createUserAddressDTO);

    @InheritInverseConfiguration(name = "toEntity")
    CreateUserAddressDTO toDto(IndirizzoUtente indirizzoUtente);

    @InheritConfiguration(name = "toEntity")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    IndirizzoUtente partialUpdate(CreateUserAddressDTO createUserAddressDTO, @MappingTarget IndirizzoUtente indirizzoUtente);

    IndirizzoUtente toEntity(UpdateUserAddressDTO updateUserAddressDTO);

    UpdateUserAddressDTO toDto1(IndirizzoUtente indirizzoUtente);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    IndirizzoUtente partialUpdate(UpdateUserAddressDTO updateUserAddressDTO, @MappingTarget IndirizzoUtente indirizzoUtente);

    IndirizzoUtente toEntity(ResponseUserAddressDTO responseUserAddressDTO);

    ResponseUserAddressDTO toDto2(IndirizzoUtente indirizzoUtente);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    IndirizzoUtente partialUpdate(ResponseUserAddressDTO responseUserAddressDTO, @MappingTarget IndirizzoUtente indirizzoUtente);
}