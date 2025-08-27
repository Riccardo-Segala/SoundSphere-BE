package backend.controller;

import backend.dto.filiale.ResponseBranchDTO;
import backend.mapper.BranchMapper;
import backend.security.CustomUserDetails;
import backend.service.FilialeService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping(path="/api/filiale", produces = MediaType.APPLICATION_JSON_VALUE)
class FilialeController {
    private final FilialeService filialeService;
    private final BranchMapper branchMapper;
    FilialeController(FilialeService filialeService, BranchMapper branchMapper) {
        this.filialeService = filialeService;
        this.branchMapper = branchMapper;
    }

    @GetMapping("/myBranch")
    public ResponseEntity<ResponseBranchDTO> getMyBranch(@AuthenticationPrincipal CustomUserDetails userDetails) {
        UUID id = userDetails.getId();
        return ResponseEntity.ok(branchMapper.toDto(filialeService.getById(id)));
    }

}
