package backend.repository;

import backend.model.Stock;
import backend.model.embeddable.FilialeProdottoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface StockRepository extends JpaRepository<Stock, FilialeProdottoId> {
}
