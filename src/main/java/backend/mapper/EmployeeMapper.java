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

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {UserMapper.class, RoleResolver.class, RoleMapper.class, BranchResolver.class})
public interface EmployeeMapper extends GenericMapper<Dipendente, CreateEmployeeDTO, UpdateEmployeeDTO, ResponseEmployeeDTO> {

    @Override

    Dipendente fromCreateDto(CreateEmployeeDTO createEmployeeDTO);

    @Override
    @Mapping(source = "filiale.id", target = "filialeId")
    ResponseEmployeeDTO toDto(Dipendente dipendente);

    @Override

    Dipendente fromUpdateDto(UpdateEmployeeDTO updateEmployeeDTO);

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)

    Dipendente partialUpdateFromCreate(CreateEmployeeDTO createEmployeeDTO, @MappingTarget Dipendente dipendente);

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)

    Dipendente partialUpdateFromUpdate(UpdateEmployeeDTO updateEmployeeDTO, @MappingTarget Dipendente dipendente);

    @Mapping(target = "dataRegistrazione", expression = "java(LocalDate.now())")
    @Mapping(source = "filialeId", target = "filiale")
    @Mapping(source = "utente", target = ".")
    @Mapping(source = "utente.ruoliIds", target = "ruoli", qualifiedByName = "findRolesByIds")
    Dipendente fromAdminCreateDto(CreateEmployeeFromAdminDTO createDTO);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "filialeId", target = "filiale")
    Dipendente partialUpdateFromAdminUpdate(UpdateEmployeeFromAdminDTO createDTO, @MappingTarget Dipendente dipendente);
}