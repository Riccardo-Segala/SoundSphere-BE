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

    @GetMapping("/{id}")
    public ResponseEntity<ResponseBranchDTO> getBranchById(@PathVariable UUID id) {
        return ResponseEntity.ok(adminFilialeService.getById(id));
    }

    @PostMapping
    public ResponseEntity<ResponseBranchDTO> createBranch(@RequestBody CreateBranchDTO createDTO) {
        return ResponseEntity.ok(adminFilialeService.createBranch(createDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseBranchDTO> updateBranch(@PathVariable UUID id, @RequestBody UpdateBranchDTO updateDTO) {
        return ResponseEntity.ok(adminFilialeService.updateBranch(id, updateDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBranch(@PathVariable UUID id) {
        return ResponseEntity.ok(adminFilialeService.deleteBranch(id));
    }
}
