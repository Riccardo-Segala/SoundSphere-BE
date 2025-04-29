package backend.mapper;

import backend.dto.dipendente.CreateEmplyeeDTO;
import backend.dto.dipendente.ResponseEmployeeDTO;
import backend.dto.dipendente.UpdateEmployeeDTO;
import backend.model.Dipendente;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface EmployeeMapper {
    @Mapping(source = "filialeId", target = "filiale.id")
    Dipendente toEntity(CreateEmplyeeDTO createEmplyeeDTO);

    @Mapping(source = "filiale.id", target = "filialeId")
    CreateEmplyeeDTO toDto(Dipendente dipendente);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "filialeId", target = "filiale.id")
    Dipendente partialUpdate(CreateEmplyeeDTO createEmplyeeDTO, @MappingTarget Dipendente dipendente);

    @Mapping(source = "filialeId", target = "filiale.id")
    Dipendente toEntity(UpdateEmployeeDTO updateEmployeeDTO);

    @Mapping(source = "filiale.id", target = "filialeId")
    UpdateEmployeeDTO toDto1(Dipendente dipendente);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "filialeId", target = "filiale.id")
    Dipendente partialUpdate(UpdateEmployeeDTO updateEmployeeDTO, @MappingTarget Dipendente dipendente);

    @Mapping(source = "filialeId", target = "filiale.id")
    @Mapping(source = "vantaggioId", target = "vantaggio.id")
    Dipendente toEntity(ResponseEmployeeDTO responseEmployeeDTO);

    @InheritInverseConfiguration(name = "toEntity")
    ResponseEmployeeDTO toDto2(Dipendente dipendente);

    @InheritConfiguration(name = "toEntity")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Dipendente partialUpdate(ResponseEmployeeDTO responseEmployeeDTO, @MappingTarget Dipendente dipendente);
}