package backend.controller.admin;

import backend.dto.ruolo.CreateRoleDTO;
import backend.dto.ruolo.ResponseRoleDTO;
import backend.dto.ruolo.UpdateRoleDTO;
import backend.service.RuoloService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping(path="/api/admin/ruoli", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminRuoloController {
    private RuoloService ruoloService;
    @GetMapping
    public ResponseEntity<List<ResponseRoleDTO>> getAllRoles() {
        return ResponseEntity.ok(ruoloService.getAllRoles());
    }

    @GetMapping("/{roleId}")
    public ResponseEntity<ResponseRoleDTO> getRoleById(@PathVariable UUID roleId) {
        return ResponseEntity.ok(ruoloService.getRoleById(roleId));
    }

    @PostMapping
    public ResponseEntity<ResponseRoleDTO> createRole(@RequestBody CreateRoleDTO createDTO) {
        return ResponseEntity.ok(ruoloService.createRole(createDTO));
    }

    @PutMapping("/{roleId}")
    public ResponseEntity<ResponseRoleDTO> updateRole(@PathVariable UUID roleId, @RequestBody UpdateRoleDTO updateDTO) {
        return ResponseEntity.ok(ruoloService.updateRole(roleId, updateDTO));
    }

    @DeleteMapping("/{roleId}")
    public ResponseEntity<Void> deleteRole(@PathVariable UUID roleId) {
        return ResponseEntity.ok(ruoloService.deleteRole(roleId));
    }
}
