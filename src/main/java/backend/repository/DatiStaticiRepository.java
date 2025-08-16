package backend.repository;

import backend.model.DatiStatici;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DatiStaticiRepository extends JpaRepository<DatiStatici, UUID> {
    // Cerca uno specifico dato statico per nome
    Optional<DatiStatici> findByNome(String nome);
}
