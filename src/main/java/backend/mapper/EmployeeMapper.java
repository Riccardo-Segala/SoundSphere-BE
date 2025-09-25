package backend.mapper;

import backend.dto.dipendente.CreateEmployeeDTO;
import backend.dto.dipendente.ResponseEmployeeDTO;
import backend.dto.dipendente.UpdateEmployeeDTO;
import backend.dto.dipendente.admin.CreateEmployeeFromAdminDTO;
import backend.dto.dipendente.admin.UpdateEmployeeFromAdminDTO;
import backend.mapper.helper.RoleAssignmentHelper;
import backend.mapper.resolver.BranchResolver;
import backend.mapper.resolver.RoleResolver;
import backend.model.Dipendente;
import backend.repository.RuoloRepository;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {RoleResolver.class, RoleMapper.class, BranchResolver.class, RuoloRepository.class})
public abstract class EmployeeMapper {

    @Autowired
    protected RoleAssignmentHelper roleAssignmentHelper;

    public abstract Dipendente fromCreateDto(CreateEmployeeDTO createEmployeeDTO);

    @Mapping(source = "filiale.id", target = "filialeId")
    @Mapping(source = "utenteRuoli", target = "ruoli", qualifiedByName = "mapUtenteRuoliToRuoliStrings")
    public abstract ResponseEmployeeDTO toDto(Dipendente dipendente);


    public abstract Dipendente fromUpdateDto(UpdateEmployeeDTO updateEmployeeDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract Dipendente partialUpdateFromCreate(CreateEmployeeDTO createEmployeeDTO, @MappingTarget Dipendente dipendente);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract Dipendente partialUpdateFromUpdate(UpdateEmployeeDTO updateEmployeeDTO, @MappingTarget Dipendente dipendente);

    @Mapping(target = "dataRegistrazione", expression = "java(LocalDate.now())")
    @Mapping(source = "filialeId", target = "filiale")
    @Mapping(source = "utente", target = ".")
    @Mapping(target = "utenteRuoli", ignore = true)
    public abstract Dipendente fromAdminCreateDto(CreateEmployeeFromAdminDTO createDTO);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "filialeId", target = "filiale")
    @Mapping(target = "utenteRuoli", ignore = true)
    public abstract Dipendente partialUpdateFromAdminUpdate(UpdateEmployeeFromAdminDTO updateDTO, @MappingTarget Dipendente dipendente);

    @AfterMapping
    protected void linkRolesToUser(UpdateEmployeeFromAdminDTO dto, @MappingTarget Dipendente dipendente) {
        // Logica delegata e ottimizzata
        roleAssignmentHelper.synchronizeRoles(dipendente, dto.ruoliIds());
    }
}