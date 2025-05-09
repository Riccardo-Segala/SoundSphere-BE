package backend.controller;

import backend.dto.filiale.CreateBranchDTO;
import backend.dto.filiale.ResponseBranchDTO;
import backend.dto.filiale.UpdateBranchDTO;
import backend.mapper.BranchMapper;
import backend.model.Filiale;
import backend.service.FilialeService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/filiale")
class FilialeController extends GenericController <Filiale, UUID, CreateBranchDTO, UpdateBranchDTO, ResponseBranchDTO> {
    public FilialeController(FilialeService service, BranchMapper mapper) {
        super(service, mapper);
    }

}
