package backend.repository;

import backend.model.Prodotto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProdottoRepository extends JpaRepository<Prodotto, UUID> {
    @Query("SELECT p FROM Prodotto p JOIN p.categorie c WHERE c.id = :categoryId")
    List<Prodotto> findByExactCategoryId(@Param("categoryId") UUID categoryId);
}