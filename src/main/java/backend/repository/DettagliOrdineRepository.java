package backend.repository;

import backend.model.DettagliOrdine;
import backend.model.embeddable.OrdineProdottoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface DettagliOrdineRepository extends JpaRepository<DettagliOrdine, OrdineProdottoId> {
}
