package backend.service;

import backend.dto.categoria.CreateCategoryDTO;
import backend.dto.categoria.ResponseCategoryNavigationDTO;
import backend.dto.categoria.ResponseParentCategoryDTO;
import backend.dto.categoria.UpdateCategoryDTO;
import backend.exception.ResourceNotFoundException;
import backend.mapper.CategoryMapper;
import backend.model.Categoria;
import backend.repository.CategoriaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.Normalizer;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

@Service
public class CategoriaService extends GenericService<Categoria, UUID>{
    private final CategoriaRepository categoriaRepository;
    private final CategoryMapper categoryMapper;
    public CategoriaService(CategoriaRepository repository, CategoryMapper categoryMapper) {
        super(repository);
        this.categoriaRepository = repository;
        this.categoryMapper = categoryMapper;
    }

    @Transactional(readOnly = true)
    public ResponseCategoryNavigationDTO findCategoryDetailsById(UUID id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Categoria non trovata con id: " + id));

        return categoryMapper.toNavigationDto(categoria);
    }

    @Transactional(readOnly = true)
    public ResponseCategoryNavigationDTO findCategoryDetailsBySlug(String slug) {
        Categoria categoria = findCategoryBySlug(slug);

        return categoryMapper.toNavigationDto(categoria);
    }

    /**
     * Trova tutte le categorie di primo livello (quelle senza un genitore).
     * Utile per popolare il menu principale della navigazione.
     * @return Una lista di DTO per la navigazione.
     */
    @Transactional(readOnly = true)
    public List<ResponseParentCategoryDTO> findTopLevelCategories() {
        List<Categoria> topLevelCategories = categoriaRepository.findByParentIsNull();

        return topLevelCategories.stream()
                .map(categoryMapper::toParentDto)
                .toList();
    }

    public List<Categoria> findAndValidateCategoriesByIds(List<UUID> ids) {
        List<Categoria> categorie = categoriaRepository.findAllById(ids);

        // Se il numero di categorie trovate è diverso dal numero di ID passati,
        // significa che almeno un ID non era valido.
        if (categorie.size() != ids.size()) {
            throw new EntityNotFoundException("Una o più categorie specificate non sono valide.");
        }

        return categorie;
    }

    @Transactional
    public Categoria create(CreateCategoryDTO dto) {

        // Variabile per tenere traccia del parent, se presente.
        Categoria parent = null;

        // Mappa il DTO a un'entità Categoria.
        Categoria categoria = categoryMapper.fromCreateDto(dto);

        // Se è specificato un parentId, recupera la categoria parent.
        if (dto.parentId() != null) {
            parent = categoriaRepository.findById(dto.parentId())
                    .orElseThrow(() -> new EntityNotFoundException("Categoria genitore non trovata"));
            categoria.setParent(parent);
        }

        // Genera uno slug unico basato sul nome e sul parent (se presente).
        String uniqueSlug = createUniqueSlug(dto.name(), parent);
        categoria.setSlug(uniqueSlug);

        // Salva la nuova categoria nel database.
        return categoriaRepository.save(categoria);
    }

    @Transactional
    public Categoria update(UUID id, UpdateCategoryDTO dto) {
        // 1. Recupera la categoria esistente o lancia un'eccezione se non trovata.
        Categoria categoriaToUpdate = categoriaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Categoria non trovata con id: " + id));

        // 2. Aggiorna il nome e lo slug, solo se il nuovo nome è fornito ed è diverso.
        if (dto.name() != null && !dto.name().isBlank() && !dto.name().equals(categoriaToUpdate.getName())) {
            categoriaToUpdate.setName(dto.name());

            // Lo slug viene rigenerato direttamente, senza alcun controllo di unicità.
            String uniqueSlug = createUniqueSlugForUpdate(dto.name(), categoriaToUpdate);
            categoriaToUpdate.setSlug(uniqueSlug);
        }

        // 3. Aggiorna il genitore (questa logica rimane invariata).
        if (dto.parentId() != null) {
            if (dto.parentId().equals(id)) {
                throw new IllegalArgumentException("Una categoria non può essere genitore di se stessa.");
            }
            Categoria parent = categoriaRepository.findById(dto.parentId())
                    .orElseThrow(() -> new EntityNotFoundException("Categoria genitore non trovata con id: " + dto.parentId()));
            categoriaToUpdate.setParent(parent);
        } else {
            categoriaToUpdate.setParent(null);
        }

        // 4. Salva le modifiche nel database.
        return categoriaRepository.save(categoriaToUpdate);
    }

    private String createUniqueSlug(String name, Categoria parent) {
        String baseSlug = normalizeSlugFromName(name);
        String finalSlug = baseSlug;

        // 1. Controlla se lo slug base esiste già
        if (categoriaRepository.existsBySlug(finalSlug)) {

            // 2. Se esiste E abbiamo un genitore, crea uno slug più specifico
            if (parent != null) {
                finalSlug = normalizeSlugFromName(parent.getName()) + "-" + baseSlug;
            }

            // 3. Come ultima spiaggia, se anche lo slug specifico esiste (o se non c'è un genitore),
            //    aggiungi un suffisso numerico finché non ne trovi uno libero.
            int counter = 2;
            String originalSlugForLoop = finalSlug;

            while (categoriaRepository.existsBySlug(finalSlug)) {
                finalSlug = originalSlugForLoop + "-" + counter;
                counter++;
            }
        }

        return finalSlug;
    }

    private String createUniqueSlugForUpdate(String name, Categoria existingCategory) {
        String baseSlug = normalizeSlugFromName(name);
        String finalSlug = baseSlug;

        // Controlla se lo slug base esiste
        Optional<Categoria> conflictingCategory = categoriaRepository.findBySlug(finalSlug);

        // Se esiste ed è diversa dalla categoria attuale, bisogna risolvere il conflitto
        if (conflictingCategory.isPresent() && !conflictingCategory.get().getId().equals(existingCategory.getId())) {

            // Usa il genitore ATTUALE della categoria per creare uno slug più specifico
            if (existingCategory.getParent() != null) {
                finalSlug = normalizeSlugFromName(existingCategory.getParent().getName()) + "-" + baseSlug;
            }

            // Se anche lo slug specifico esiste (o se non c'è un genitore),
            // aggiungo un suffisso numerico finché non ne trovo uno libero.
            int counter = 2;
            String originalSlugForLoop = finalSlug;
            while(categoriaRepository.findBySlug(finalSlug).filter(c -> !c.getId().equals(existingCategory.getId())).isPresent()) {
                finalSlug = originalSlugForLoop + "-" + counter;
                counter++;
            }
        }

        return finalSlug;
    }

    private String normalizeSlugFromName(String name) {
        // logica per pulire la stringa
        if (name == null) return "";
        String slug = Normalizer.normalize(name, Normalizer.Form.NFD);
        slug = slug.replaceAll("\\p{M}", ""); // Rimuove accenti
        slug = slug.toLowerCase(Locale.ITALIAN);
        slug = slug.replaceAll("\\s+", "-"); // Sostituisce spazi
        slug = slug.replaceAll("[^a-z0-9-]", ""); // Rimuove caratteri non validi
        slug = slug.replaceAll("-+", "-"); // Rimuove trattini duplicati
        slug = slug.replaceAll("^-|-$", ""); // Rimuove trattini all'inizio/fine
        return slug;
    }

    public Categoria findCategoryBySlug(String slug) {
        return categoriaRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria non trovata con slug: " + slug));
    }
}
