package backend.controller;

import backend.dto.vantaggio.CreateBenefitDTO;
import backend.dto.vantaggio.ResponseBenefitDTO;
import backend.dto.vantaggio.UpdateBenefitDTO;
import backend.mapper.BenefitMapper;
import backend.model.Vantaggio;
import backend.service.VantaggioService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/vantaggio")
public class VantaggioController extends GenericController<Vantaggio, UUID, CreateBenefitDTO, UpdateBenefitDTO, ResponseBenefitDTO> {
    public VantaggioController(VantaggioService service, BenefitMapper mapper) {
        super(service, mapper);
    }
}