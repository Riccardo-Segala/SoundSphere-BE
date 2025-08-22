package backend.repository;

import backend.model.Carrello;
import backend.model.embeddable.UtenteProdottoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CarrelloRepository extends JpaRepository<Carrello, UtenteProdottoId> {

    // Trova tutte le righe del carrello per un utente specifico, non della wishlist
    @Query("SELECT c FROM Carrello c JOIN FETCH c.prodotto WHERE c.utente.id = :utenteId AND c.wishlist = false")
    List<Carrello> findCartByUserId(@Param("utenteId") UUID utenteId);

    @Query("SELECT c FROM Carrello c JOIN FETCH c.prodotto WHERE c.utente.id = :utenteId AND c.wishlist = true")
    List<Carrello> findWishlistByUserId(@Param("utenteId")UUID utenteId);
}