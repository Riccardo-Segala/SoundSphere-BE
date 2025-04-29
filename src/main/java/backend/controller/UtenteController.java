package backend.controller;

import backend.dto.utente.CreateUserDTO;
import backend.dto.utente.ResponseUserDTO;
import backend.dto.utente.UpdateUserDTO;
import backend.mapper.UserMapper;
import backend.model.Utente;
import backend.service.UtenteService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/utenti")
public class UtenteController extends GenericController<Utente, UUID, CreateUserDTO, UpdateUserDTO, ResponseUserDTO> {
    public UtenteController(UtenteService service, UserMapper mapper) {
        super(service, mapper);
    }
}