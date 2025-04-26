package backend.repository;

import backend.model.Filiale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FilialeRepository extends JpaRepository<Filiale, UUID> {
}