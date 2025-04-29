package backend.repository;

import backend.model.Vantaggio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface VantaggioRepository extends JpaRepository<Vantaggio, UUID> {
}
