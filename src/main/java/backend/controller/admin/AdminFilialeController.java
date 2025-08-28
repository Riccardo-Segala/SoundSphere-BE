package backend.controller.admin;

import backend.dto.filiale.CreateBranchDTO;
import backend.dto.filiale.ResponseBranchDTO;
import backend.dto.filiale.UpdateBranchDTO;
import backend.mapper.BranchMapper;
import backend.service.admin.AdminFilialeService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path="/api/admin/filiale", produces = MediaType.APPLICATION_JSON_VALUE)
class AdminFilialeController{

    final AdminFilialeService adminFilialeService;
    final BranchMapper branchMapper;
    AdminFilialeController(AdminFilialeService service, BranchMapper mapper) {
        this.adminFilialeService = service;
        this.branchMapper = mapper;
    }


    @GetMapping
    public ResponseEntity<List<ResponseBranchDTO>> getAllBranches() {
        return ResponseEntity.ok(adminFilialeService.getAll());
    }

    @GetMapping("/{branchId}")
    public ResponseEntity<ResponseBranchDTO> getBranchById(@PathVariable UUID branchId) {
        return ResponseEntity.ok(adminFilialeService.getById(branchId));
    }

    @PostMapping
    public ResponseEntity<ResponseBranchDTO> createBranch(@RequestBody CreateBranchDTO createDTO) {
        return ResponseEntity.ok(adminFilialeService.createBranch(createDTO));
    }

    @PutMapping("/{branchId}")
    public ResponseEntity<ResponseBranchDTO> updateBranch(@PathVariable UUID branchId, @RequestBody UpdateBranchDTO updateDTO) {
        return ResponseEntity.ok(adminFilialeService.updateBranch(branchId, updateDTO));
    }

    @DeleteMapping("/{branchId}")
    public ResponseEntity<Void> deleteBranch(@PathVariable UUID branchId) {
        return ResponseEntity.ok(adminFilialeService.deleteBranch(branchId));
    }
}
