package backend.repository;

import backend.model.MetodoPagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MetodoPagamentoRepository extends JpaRepository<MetodoPagamento, UUID> {

    List<MetodoPagamento> findByUtente_Id(UUID utenteId);

    Optional<MetodoPagamento> findByIdAndUtenteId(UUID methodId, UUID utenteId);

}
