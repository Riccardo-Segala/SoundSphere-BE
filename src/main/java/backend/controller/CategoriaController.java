package backend.controller;

import backend.dto.categoria.*;
import backend.mapper.CategoryMapper;
import backend.model.Categoria;
import backend.service.CategoriaService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path="/api/categorie", produces = MediaType.APPLICATION_JSON_VALUE)
public class CategoriaController extends GenericController <Categoria, UUID, CreateCategoryDTO, UpdateCategoryDTO, ResponseCategoryDTO>{
    private final CategoriaService categoriaService;
    private final CategoryMapper categoryMapper;
    public CategoriaController(CategoriaService service, CategoryMapper mapper) {
        super(service, mapper);
        this.categoriaService = service;
        this.categoryMapper = mapper;
    }

    // --- ENDPOINT PUBBLICI (Senza @PreAuthorize) ---

    /**
     * Endpoint pubblico per la navigazione passo-passo.
     * Restituisce i dettagli e i figli diretti di una categoria.
     */
    @GetMapping("/byId/{id}")
    public ResponseEntity<ResponseCategoryNavigationDTO> getCategoryDetailsById(@PathVariable UUID id) {
        ResponseCategoryNavigationDTO details = categoriaService.findCategoryDetailsById(id);
        return ResponseEntity.ok(details);
    }

    @GetMapping("/bySlug/{slug}")
    public ResponseEntity<ResponseCategoryNavigationDTO> getCategoryDetailsBySlug(@PathVariable String slug) {
        ResponseCategoryNavigationDTO details = categoriaService.findCategoryDetailsBySlug(slug);
        return ResponseEntity.ok(details);
    }

    /**
     * Endpoint pubblico per ottenere le categorie principali (quelle senza genitore).
     */
    @GetMapping
    public ResponseEntity<List<ResponseParentCategoryDTO>> getTopLevelCategories() {
        List<ResponseParentCategoryDTO> topLevel = categoriaService.findTopLevelCategories();
        return ResponseEntity.ok(topLevel);
    }


    // --- ENDPOINT ADMIN (Protetti con @PreAuthorize) ---

    /**
     * Endpoint admin per creare una nuova categoria.
     */
    @PostMapping
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseCategoryDTO> createCategory(@Valid @RequestBody CreateCategoryDTO dto) {
        Categoria savedEntity = categoriaService.create(dto);


        ResponseCategoryDTO responseDto = categoryMapper.toDto(savedEntity);

        // Costruisco l'URI per l'header 'Location' della risposta 201 Created
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedEntity.getId()).toUri();

        return ResponseEntity.created(location).body(responseDto);
    }

    /**
     * Endpoint admin per aggiornare una categoria.
     * Anche questo FA L'OVERRIDE per restituire il DTO corretto.
     */
    @PutMapping("/{id}")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseCategoryDTO> updateCategory(@PathVariable UUID id, @Valid @RequestBody UpdateCategoryDTO dto) {

        Categoria updatedEntity = categoriaService.update(id, dto);

        // Converto l'entità aggiornata nel DTO di risposta standard
        ResponseCategoryDTO responseDto = categoryMapper.toDto(updatedEntity);

        return ResponseEntity.ok(responseDto);
    }

    /**
     * Endpoint admin per eliminare una categoria.
     */
    @DeleteMapping("/{id}")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCategory(@PathVariable UUID id) {
        return super.delete(id); // Usa la logica generica
    }

    /**
     * Endpoint admin per ottenere la visione completa e ricorsiva di una categoria.
     * Nota: URL diverso per non andare in conflitto con quello pubblico.
     */

    @Override
    protected UUID getId(Categoria entity) {
        return null;
    }
}
