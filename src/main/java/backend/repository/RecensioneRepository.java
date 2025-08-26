package backend.repository;

import backend.model.Recensione;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RecensioneRepository extends JpaRepository<Recensione, UUID> {
    List<Recensione> findByProdottoId(UUID productId);

    @Query("SELECT COALESCE(AVG(r.numStelle), 0) FROM Recensione r WHERE r.prodotto.id = :prodottoId")
    Double findAverageStarsByProdottoId(@Param("prodottoId") UUID prodottoId);
}