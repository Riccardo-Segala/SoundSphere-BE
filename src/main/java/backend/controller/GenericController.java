package backend.controller;

import backend.mapper.GenericMapper;
import backend.service.GenericService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class GenericController<T, ID, CreateDTO, UpdateDTO, ResponseDTO> {

    private final GenericService<T, ID> service;
    private final GenericMapper<T, CreateDTO, UpdateDTO, ResponseDTO> mapper;

    protected GenericController(GenericService<T, ID> service, GenericMapper<T, CreateDTO, UpdateDTO, ResponseDTO> mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    public List<ResponseDTO> getAll() {
        return service.getAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO> getById(@PathVariable ID id) {
        Optional<T> entity = service.findById(id);
        return entity.map(e -> ResponseEntity.ok(mapper.toDto(e)))
                     .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseDTO create(@RequestBody CreateDTO createDTO) {
        T entity = mapper.fromCreateDto(createDTO);
        return mapper.toDto(service.create(entity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO> update(@PathVariable ID id, @RequestBody UpdateDTO updateDTO) {
        if (service.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        T entity = mapper.fromUpdateDto(updateDTO);
        return ResponseEntity.ok(mapper.toDto(service.update(entity, id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable ID id) {
        if (service.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}