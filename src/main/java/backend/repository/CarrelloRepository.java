package backend.repository;

import backend.model.Carrello;
import backend.model.embeddable.UtenteProdottoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CarrelloRepository extends JpaRepository<Carrello, UUID> {
}