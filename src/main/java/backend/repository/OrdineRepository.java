package backend.repository;

import backend.model.Ordine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrdineRepository extends JpaRepository<Ordine, UUID> {

    @Query("SELECT o FROM Ordine o JOIN FETCH o.dettagli d JOIN FETCH d.prodotto WHERE o.utente.id = :utenteId ORDER BY o.dataAcquisto ASC")
    List<Ordine> findByUtenteId(UUID utenteId);

    @Override
    @Query("SELECT o FROM Ordine o JOIN FETCH o.dettagli d JOIN FETCH d.prodotto ORDER BY o.dataAcquisto DESC")
    @NonNull
    List<Ordine> findAll();
}