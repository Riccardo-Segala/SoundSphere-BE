package backend.repository;

import backend.model.Vantaggio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface VantaggioRepository extends JpaRepository<Vantaggio, UUID> {

    // Trova un vantaggio in base al punteggio dell'utente
    @Query("SELECT v FROM Vantaggio v WHERE :punti BETWEEN v.punteggioMinimo AND v.punteggioMassimo")
    Optional<Vantaggio> findVantaggioByPunteggio(@Param("punti") Integer puntiUtente);
}
