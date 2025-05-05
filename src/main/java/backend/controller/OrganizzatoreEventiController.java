package backend.controller;

import backend.dto.organizzatore_eventi.CreateEventManagerDTO;
import backend.dto.organizzatore_eventi.ResponseEventManagerDTO;
import backend.dto.organizzatore_eventi.UpdateEventManagerDTO;
import backend.mapper.EventManagerMapper;
import backend.model.OrganizzatoreEventi;
import backend.service.OrganizzatoreEventiService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/organizzatore-eventi")
class OrganizzatoreEventiController extends GenericController<OrganizzatoreEventi, UUID, CreateEventManagerDTO, UpdateEventManagerDTO, ResponseEventManagerDTO> {
    public OrganizzatoreEventiController(OrganizzatoreEventiService service, EventManagerMapper mapper) {
        super(service, mapper);
    }

}
