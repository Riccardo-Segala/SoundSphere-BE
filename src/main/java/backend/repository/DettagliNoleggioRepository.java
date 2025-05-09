package backend.repository;

import backend.model.DettagliNoleggio;
import backend.model.embeddable.NoleggioProdottoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DettagliNoleggioRepository extends JpaRepository<DettagliNoleggio, NoleggioProdottoId> {
}