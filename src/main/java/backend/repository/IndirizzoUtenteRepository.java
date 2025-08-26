package backend.repository;

import backend.model.IndirizzoUtente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IndirizzoUtenteRepository extends JpaRepository<IndirizzoUtente, UUID> {
    List<IndirizzoUtente> findByUtenteId(UUID userId);

    @Query ("SELECT iu FROM IndirizzoUtente iu WHERE iu.id = :addressId AND iu.utente.id = :authUserId")
    Optional<IndirizzoUtente> findByIdAndUtenteId(UUID addressId, UUID authUserId);

    Optional<IndirizzoUtente> findByUtenteIdAndMainTrue(UUID userId);
}