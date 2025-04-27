package backend.repository;

import backend.model.IndirizzoUtente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IndirizzoUtenteRepository extends JpaRepository<IndirizzoUtente, UUID> {
}