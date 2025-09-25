package backend.controller;
import backend.dto.filiale.ResponseBranchDTO;
import backend.mapper.EmployeeMapper;
import backend.security.CustomUserDetails;
import backend.service.DipendenteService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path="/api/dipendenti", produces = MediaType.APPLICATION_JSON_VALUE)
class DipendenteController {
    private final DipendenteService dipendenteService;
    private final EmployeeMapper employeeMapper;
    public DipendenteController(DipendenteService service, EmployeeMapper mapper) {
        this.dipendenteService = service;
        this.employeeMapper = mapper;
    }


    @GetMapping("/myBranch")
    @PreAuthorize("hasAuthority('GESTIONE_STOCK')")
    public ResponseEntity<ResponseBranchDTO> getMyBranch(@AuthenticationPrincipal CustomUserDetails userDetails) {
        UUID id = userDetails.getId();
        return ResponseEntity.ok(dipendenteService.getMyBranch(id));
    }

}
