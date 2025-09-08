package backend.service;

import backend.dto.ruolo.CreateRoleDTO;
import backend.dto.ruolo.ResponseRoleDTO;
import backend.dto.ruolo.UpdateRoleDTO;
import backend.exception.ResourceNotFoundException;
import backend.mapper.RoleMapper;
import backend.model.Ruolo;
import backend.repository.RuoloRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RuoloService {
    private final RuoloRepository ruoloRepository;
    private final RoleMapper roleMapper;

    public Ruolo findByName(String roleName) {
        String roleNameUpperCase = roleName.toUpperCase();
        return ruoloRepository.findByNome(roleNameUpperCase)
                .orElseThrow(() -> new IllegalStateException("Ruolo '" + roleNameUpperCase + "' non trovato"));
    }

    public List<ResponseRoleDTO> getAllRoles() {
        List<Ruolo> ruoli = ruoloRepository.findAll();
        return ruoli.stream()
                .map(roleMapper::toDto)
                .toList();
    }

    public ResponseRoleDTO getRoleById(UUID roleId) {
        Ruolo ruolo = ruoloRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Ruolo con ID '" + roleId + "' non trovato"));
        return roleMapper.toDto(ruolo);
    }

    public ResponseRoleDTO createRole(CreateRoleDTO createDTO) {
        Ruolo ruolo = roleMapper.fromCreateDto(createDTO);
        Ruolo savedRuolo = ruoloRepository.save(ruolo);
        return roleMapper.toDto(savedRuolo);
    }

    // da sviluppare
    public ResponseRoleDTO updateRole(UUID roleId, UpdateRoleDTO updateDTO) {
        return new ResponseRoleDTO(null,null,null);
    }

    public Void deleteRole(UUID roleId) {
        return null;
    }
}
