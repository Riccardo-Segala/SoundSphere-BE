package backend.repository;

import backend.model.Stock;
import backend.model.embeddable.FilialeProdottoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface StockRepository extends JpaRepository<Stock, FilialeProdottoId> {

    //Restituisce la lista di marche dei prodotti presenti in una determinata filiale
    @Query("SELECT DISTINCT s.prodotto.marca FROM Stock s WHERE s.filiale.nome = :nomeFiliale")
    List<String> findDistinctMarcaByFilialeNome(@Param("nomeFiliale") String nomeFiliale);
}
