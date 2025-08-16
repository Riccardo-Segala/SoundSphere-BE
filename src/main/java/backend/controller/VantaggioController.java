package backend.controller;

import backend.dto.vantaggio.CreateBenefitDTO;
import backend.dto.vantaggio.ResponseBenefitDTO;
import backend.dto.vantaggio.UpdateBenefitDTO;
import backend.mapper.BenefitMapper;
import backend.model.Vantaggio;
import backend.service.VantaggioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/vantaggio")
public class VantaggioController extends GenericController<Vantaggio, UUID, CreateBenefitDTO, UpdateBenefitDTO, ResponseBenefitDTO> {
    public VantaggioController(VantaggioService service, BenefitMapper mapper) {
        super(service, mapper);
    }

    @GetMapping
    public ResponseEntity<List<ResponseBenefitDTO>> getAllBenefits() {
        return super.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseBenefitDTO> getBenefitById(@PathVariable UUID id) {
        return super.getById(id);
    }

    @PostMapping
    public ResponseEntity<ResponseBenefitDTO> createBenefit(@RequestBody CreateBenefitDTO createDTO) {
        return super.create(createDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseBenefitDTO> updateBenefit(@PathVariable UUID id, @RequestBody UpdateBenefitDTO updateDTO) {
        return super.update(id, updateDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBenefit(@PathVariable UUID id) {
        return super.delete(id);
    }

    @Override
    protected UUID getId(Vantaggio entity) {
        return entity.getId();
    }
}