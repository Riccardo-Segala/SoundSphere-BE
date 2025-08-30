package backend.mapper;

import backend.dto.ruolo.CreateRoleDTO;
import backend.dto.ruolo.ResponseRoleDTO;
import backend.dto.ruolo.UpdateRoleDTO;
import backend.model.Ruolo;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.UUID;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface RoleMapper extends GenericMapper<Ruolo, CreateRoleDTO, UpdateRoleDTO, ResponseRoleDTO> {
    default UUID idfromRuolo(Ruolo ruolo) {
        return ruolo != null ? ruolo.getId() : null;
    }

    default String namefromRuolo(Ruolo ruolo) {
        return ruolo != null ? ruolo.getNome() : null;
    }
}
