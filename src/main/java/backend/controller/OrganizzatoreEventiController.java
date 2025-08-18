package backend.controller;

import backend.dto.organizzatore_eventi.CreateEventManagerDTO;
import backend.dto.organizzatore_eventi.ResponseEventManagerDTO;
import backend.dto.organizzatore_eventi.UpdateEventManagerDTO;
import backend.mapper.EventManagerMapper;
import backend.model.OrganizzatoreEventi;
import backend.service.OrganizzatoreEventiService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path="/api/organizzatore-eventi", produces = MediaType.APPLICATION_JSON_VALUE)
class OrganizzatoreEventiController extends GenericController<OrganizzatoreEventi, UUID, CreateEventManagerDTO, UpdateEventManagerDTO, ResponseEventManagerDTO> {
    public OrganizzatoreEventiController(OrganizzatoreEventiService service, EventManagerMapper mapper) {
        super(service, mapper);
    }

    @GetMapping
    public ResponseEntity<List<ResponseEventManagerDTO>> getAllEventManager() {
        return super.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseEventManagerDTO> getEventManagerById(@PathVariable UUID id) {
        return super.getById(id);
    }

    @PostMapping
    public ResponseEntity<ResponseEventManagerDTO> createEventManager(@RequestBody CreateEventManagerDTO createDTO) {
        return super.create(createDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseEventManagerDTO> updateEventManager(@PathVariable UUID id, @RequestBody UpdateEventManagerDTO updateDTO) {
        return super.update(id, updateDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEventManager(@PathVariable UUID id) {
        return super.delete(id);
    }

    @Override
    protected UUID getId(OrganizzatoreEventi entity) {
        return entity.getId();
    }

}
