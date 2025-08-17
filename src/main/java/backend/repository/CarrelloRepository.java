package backend.repository;

import backend.model.Carrello;
import backend.model.embeddable.UtenteProdottoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CarrelloRepository extends JpaRepository<Carrello, UtenteProdottoId> {

    // Trova tutte le righe del carrello per un utente specifico
    @Query("SELECT c FROM Carrello c WHERE c.id.utenteId = :utenteId")
    List<Carrello> findByUtenteId(@Param("utenteId") UUID utenteId);
}