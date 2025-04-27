package backend.repository;

import backend.model.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UtenteRepository extends JpaRepository<Utente, UUID> {
}