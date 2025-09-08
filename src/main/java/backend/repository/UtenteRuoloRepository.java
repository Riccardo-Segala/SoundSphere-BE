package backend.repository;

import backend.model.Utente;
import backend.model.UtenteRuolo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Repository
public interface UtenteRuoloRepository extends JpaRepository<UtenteRuolo, UUID> {
    @Transactional
    void deleteByDataScadenzaBefore(LocalDate now);


    List<UtenteRuolo> findByUtenteIn(List<Utente> users);
}
