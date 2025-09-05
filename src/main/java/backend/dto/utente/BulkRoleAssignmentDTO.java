package backend.dto.utente;

import java.io.Serializable;
import java.util.List;

public record BulkRoleAssignmentDTO(
        List<RoleAssignmentDTO> roleAssignments
)implements Serializable {}
