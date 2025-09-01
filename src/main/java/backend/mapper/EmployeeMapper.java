package backend.mapper;

import backend.dto.dipendente.CreateEmployeeDTO;
import backend.dto.dipendente.ResponseEmployeeDTO;
import backend.dto.dipendente.UpdateEmployeeDTO;
import backend.dto.dipendente.admin.CreateEmployeeFromAdminDTO;
import backend.dto.dipendente.admin.UpdateEmployeeFromAdminDTO;
import backend.mapper.resolver.BranchResolver;
import backend.mapper.resolver.RoleResolver;
import backend.model.Dipendente;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {RoleResolver.class, RoleMapper.class, BranchResolver.class})
public interface EmployeeMapper extends UserMapper {


    Dipendente fromCreateDto(CreateEmployeeDTO createEmployeeDTO);

    @Mapping(source = "filiale.id", target = "filialeId")
    @Mapping(source = "utenteRuoli", target = "ruoli", qualifiedByName = "mapUtenteRuoliToRuoliStrings")
    ResponseEmployeeDTO toDto(Dipendente dipendente);


    Dipendente fromUpdateDto(UpdateEmployeeDTO updateEmployeeDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)

    Dipendente partialUpdateFromCreate(CreateEmployeeDTO createEmployeeDTO, @MappingTarget Dipendente dipendente);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)

    Dipendente partialUpdateFromUpdate(UpdateEmployeeDTO updateEmployeeDTO, @MappingTarget Dipendente dipendente);

    @Mapping(target = "dataRegistrazione", expression = "java(LocalDate.now())")
    @Mapping(source = "filialeId", target = "filiale")
    @Mapping(source = "utente", target = ".")
    @Mapping(target = "utenteRuoli", ignore = true)
    Dipendente fromAdminCreateDto(CreateEmployeeFromAdminDTO createDTO);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "filialeId", target = "filiale")
    @Mapping(target = "utenteRuoli", ignore = true)
    Dipendente partialUpdateFromAdminUpdate(UpdateEmployeeFromAdminDTO createDTO, @MappingTarget Dipendente dipendente);
}