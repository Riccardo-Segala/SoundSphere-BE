package backend.controller;

import backend.mapper.BranchMapper;
import backend.service.FilialeService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path="/api/filiale", produces = MediaType.APPLICATION_JSON_VALUE)
class FilialeController {
    private final FilialeService filialeService;
    private final BranchMapper branchMapper;
    FilialeController(FilialeService filialeService, BranchMapper branchMapper) {
        this.filialeService = filialeService;
        this.branchMapper = branchMapper;
    }

}
