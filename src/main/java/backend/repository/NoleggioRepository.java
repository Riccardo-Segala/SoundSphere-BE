package backend.repository;

import backend.model.Noleggio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface NoleggioRepository extends JpaRepository<Noleggio, UUID> {
}