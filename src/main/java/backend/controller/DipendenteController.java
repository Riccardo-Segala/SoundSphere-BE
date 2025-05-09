package backend.controller;

import backend.dto.dipendente.CreateEmployeeDTO;
import backend.dto.dipendente.ResponseEmployeeDTO;
import backend.dto.dipendente.UpdateEmployeeDTO;
import backend.mapper.EmployeeMapper;
import backend.model.Dipendente;
import backend.service.DipendenteService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/dipendenti")
class DipendenteController extends GenericController <Dipendente, UUID, CreateEmployeeDTO, UpdateEmployeeDTO, ResponseEmployeeDTO> {
    public DipendenteController(DipendenteService service, EmployeeMapper mapper) {
        super(service, mapper);
    }

}
