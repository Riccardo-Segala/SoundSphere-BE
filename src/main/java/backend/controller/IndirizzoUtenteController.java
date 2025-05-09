package backend.controller;

import backend.dto.indirizzo_utente.CreateUserAddressDTO;
import backend.dto.indirizzo_utente.ResponseUserAddressDTO;
import backend.dto.indirizzo_utente.UpdateUserAddressDTO;
import backend.mapper.UserAddressMapper;
import backend.model.IndirizzoUtente;
import backend.service.IndirizzoUtenteService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/indirizzi-utente")
class IndirizzoUtenteController extends GenericController<IndirizzoUtente, UUID, CreateUserAddressDTO, UpdateUserAddressDTO, ResponseUserAddressDTO> {
    public IndirizzoUtenteController(IndirizzoUtenteService service, UserAddressMapper mapper) {
        super(service, mapper);
    }
}
