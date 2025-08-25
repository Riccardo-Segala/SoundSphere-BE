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

    @Query("SELECT p FROM Prodotto p JOIN Stock s ON p.id = s.prodotto.id WHERE s.filiale.id = :filialeId AND s.quantita > 0")
    List<Prodotto> getProductInStockByBranchId(@Param("filialeId") UUID filialeId);

    @Query("SELECT DISTINCT s.prodotto.marca FROM Stock s WHERE s.filiale.nome = :nomeFiliale")
    List<String> findDistinctMarcaByFilialeNome(@Param("nomeFiliale") String nomeFiliale);

    @Query("""
        SELECT p, s
        FROM Prodotto p
        LEFT JOIN Stock s ON p.id = s.prodotto.id AND s.filiale.id = :branchId
    """)
    List<Object[]> findAllProductsWithStockInfoByBranchId(@Param("branchId") UUID branchId);
}