package backend.repository;

import backend.model.Noleggio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface NoleggioRepository extends JpaRepository<Noleggio, UUID> {
    @Query("SELECT n FROM Noleggio n JOIN FETCH n.dettagli d JOIN FETCH d.prodotto WHERE n.utente.id = :utenteId ORDER BY n.dataInizio ASC")
    List<Noleggio> findByUtenteId(UUID utenteId);

    @Override
    @Query("SELECT n FROM Noleggio n JOIN FETCH n.dettagli d JOIN FETCH d.prodotto ORDER BY n.dataInizio DESC")
    @NonNull
    List<Noleggio> findAll();
}