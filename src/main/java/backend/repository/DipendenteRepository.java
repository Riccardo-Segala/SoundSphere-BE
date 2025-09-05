package backend.repository;

import backend.model.Dipendente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DipendenteRepository extends JpaRepository<Dipendente, UUID> {
    @Query("SELECT d FROM Dipendente d LEFT JOIN FETCH d.utenteRuoli ur LEFT JOIN FETCH ur.ruolo WHERE d.id = :id")
    Optional<Dipendente> findDipendenteByIdWithDetails(@Param("id") UUID id);
}