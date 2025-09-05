package backend.repository;

import backend.model.Ruolo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface RuoloRepository extends JpaRepository<Ruolo, UUID> {
    Optional<Ruolo> findByNome(String nome);
    @Query("SELECT r FROM Ruolo r WHERE r.id IN :ids")
    List<Ruolo> findAllByIdExplicit(@Param("ids") Set<UUID> ids);

    Set<Ruolo> findAllByIdIn(Collection<UUID> ids);
}