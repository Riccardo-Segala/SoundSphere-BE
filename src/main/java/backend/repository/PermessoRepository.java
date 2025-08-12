package backend.repository;

import backend.model.Permesso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PermessoRepository extends JpaRepository<Permesso, UUID> {
}