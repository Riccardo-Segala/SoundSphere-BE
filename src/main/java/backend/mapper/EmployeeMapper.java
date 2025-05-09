package backend.mapper;

import backend.dto.dipendente.CreateEmployeeDTO;
import backend.dto.dipendente.ResponseEmployeeDTO;
import backend.dto.dipendente.UpdateEmployeeDTO;
import backend.model.Dipendente;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface EmployeeMapper extends GenericMapper<Dipendente, CreateEmployeeDTO, UpdateEmployeeDTO, ResponseEmployeeDTO> {

    @Override
    @Mapping(source = "filialeId", target = "filiale.id")
    Dipendente fromCreateDto(CreateEmployeeDTO createEmployeeDTO);

    @Override
    @Mapping(source = "filiale.id", target = "filialeId")
    ResponseEmployeeDTO toDto(Dipendente dipendente);

    @Override
    @Mapping(source = "filialeId", target = "filiale.id")
    Dipendente fromUpdateDto(UpdateEmployeeDTO updateEmployeeDTO);

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "filialeId", target = "filiale.id")
    Dipendente partialUpdateFromCreate(CreateEmployeeDTO createEmployeeDTO, @MappingTarget Dipendente dipendente);

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "filialeId", target = "filiale.id")
    Dipendente partialUpdateFromUpdate(UpdateEmployeeDTO updateEmployeeDTO, @MappingTarget Dipendente dipendente);
}