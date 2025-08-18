package backend.controller;
import backend.dto.dettagli_noleggio.CreateRentalDetailsDTO;
import backend.dto.dettagli_noleggio.ResponseRentalDetailsDTO;
import backend.dto.dettagli_noleggio.UpdateRentalDetailsDTO;
import backend.mapper.RentalDetailsMapper;
import backend.model.DettagliNoleggio;
import backend.model.embeddable.NoleggioProdottoId;
import backend.service.DettagliNoleggioService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path="/api/dettagli-noleggi", produces = MediaType.APPLICATION_JSON_VALUE)
class DettagliNoleggioController extends GenericController<DettagliNoleggio, NoleggioProdottoId, CreateRentalDetailsDTO, UpdateRentalDetailsDTO, ResponseRentalDetailsDTO> {
    public DettagliNoleggioController(DettagliNoleggioService service, RentalDetailsMapper mapper) {
        super(service, mapper);
    }

    @GetMapping
    public ResponseEntity<List<ResponseRentalDetailsDTO>> getAllRentalDetails() {
        return super.getAll();
    }

    // GET by ID composto
    @GetMapping("/{noleggioId}/{prodottoId}")
    public ResponseEntity<ResponseRentalDetailsDTO> getRentalDetailsById(
            @PathVariable UUID noleggioId,
            @PathVariable UUID prodottoId) {

        NoleggioProdottoId id = new NoleggioProdottoId(noleggioId, prodottoId);
        return super.getById(id);
    }

    // POST
    @PostMapping
    public ResponseEntity<ResponseRentalDetailsDTO> createRentalDetails(@RequestBody CreateRentalDetailsDTO createDTO) {
        return super.create(createDTO);
    }

    // PUT by ID composto
    @PutMapping("/{noleggioId}/{prodottoId}")
    public ResponseEntity<ResponseRentalDetailsDTO> updateRentalDetails(
            @PathVariable UUID noleggioId,
            @PathVariable UUID prodottoId,
            @RequestBody UpdateRentalDetailsDTO updateDTO) {

        NoleggioProdottoId id = new NoleggioProdottoId(noleggioId, prodottoId);
        return super.update(id, updateDTO);
    }

    // DELETE by ID composto
    @DeleteMapping("/{noleggioId}/{prodottoId}")
    public ResponseEntity<Void> deleteRentalDetails(
            @PathVariable UUID noleggioId,
            @PathVariable UUID prodottoId) {

        NoleggioProdottoId id = new NoleggioProdottoId(noleggioId, prodottoId);
        return super.delete(id);
    }

    // implementazione del metodo astratto getId
    @Override
    protected NoleggioProdottoId getId(DettagliNoleggio entity) {
        return entity.getId();
    }

}
