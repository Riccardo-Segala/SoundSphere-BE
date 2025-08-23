package backend.controller;

import backend.mapper.GenericMapper;
import backend.service.GenericService;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;
@Getter
public abstract class GenericController<T, ID, CreateDTO, UpdateDTO, ResponseDTO> {

    private final GenericService<T, ID> service;
    private final GenericMapper<T, CreateDTO, UpdateDTO, ResponseDTO> mapper;

    protected GenericController(GenericService<T, ID> service, GenericMapper<T, CreateDTO, UpdateDTO, ResponseDTO> mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    protected ResponseEntity<List<ResponseDTO>> getAll() {
        List<ResponseDTO> result = service.getAll().stream()
                .map(mapper::toDto)
                .toList();

        if (result.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204 senza body
        }

        return ResponseEntity.ok(result); // 200 con body
    }

    protected ResponseEntity<ResponseDTO> getById(@PathVariable ID id) {
        Optional<T> entity = service.findById(id);
        return entity.map(e -> ResponseEntity.ok(mapper.toDto(e)))
                     .orElseGet(() -> ResponseEntity.notFound().build());
    }

    protected ResponseEntity<ResponseDTO> create(CreateDTO createDTO) {
        T entity = mapper.fromCreateDto(createDTO);
        T saved = service.create(entity);
        ResponseDTO dto = mapper.toDto(saved);

        // costruzione dell’URI della risorsa creata
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest() // Prende l'URI della richiesta (es: POST /filiali)
                .build()              // Costruisce l'URI così com'è, senza aggiungere altro
                .toUri();

        return ResponseEntity.created(location) // HTTP 201 Created + Location header
                .body(dto);
    }

    protected ResponseEntity<ResponseDTO> update(@PathVariable ID id, @RequestBody UpdateDTO updateDTO) {
        if (service.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        T entity = mapper.fromUpdateDto(updateDTO);
        return ResponseEntity.ok(mapper.toDto(service.update(entity, id)));
    }

    protected ResponseEntity<Void> delete(@PathVariable ID id) {
        if (service.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    // espongo il service
    protected GenericService<T, ID> getService() {
        return this.service;
    }

    protected abstract ID getId(T entity);
}