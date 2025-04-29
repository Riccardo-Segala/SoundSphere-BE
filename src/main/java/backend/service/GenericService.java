package backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public abstract class GenericService<T, ID> {

    private final JpaRepository<T, ID> repository;

    public GenericService(JpaRepository<T, ID> repository) {
        this.repository = repository;
    }

    public T create(T entity) {
        return repository.save(entity);
    }

    public T getById(ID id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Entità non trovata"));
    }

    public List<T> getAll() {
        return repository.findAll();
    }

    public T update(T entity, ID id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Entità non trovata");
        }
        return repository.save(entity);
    }

    public void delete(ID id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Entità non trovata");
        }
        repository.deleteById(id);
    }

    public Optional<T> findById(ID id) {
        return repository.findById(id);
    }
}