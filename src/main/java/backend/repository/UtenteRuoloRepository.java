package backend.repository;

import backend.model.UtenteRuolo;
import backend.model.embeddable.UtenteRuoloId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Repository
public interface UtenteRuoloRepository extends JpaRepository<UtenteRuolo, UtenteRuoloId> {
    @Transactional
    void deleteByDataScadenzaBefore(LocalDate now);
}
