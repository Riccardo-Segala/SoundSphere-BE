package backend.repository;

import backend.model.MetodoPagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MetodoPagamentoRepository extends JpaRepository<MetodoPagamento, UUID> {

    List<MetodoPagamento> findByUtente_Id(UUID utenteId);

    Optional<MetodoPagamento> findByIdAndUtenteId(UUID methodId, UUID utenteId);

    @Modifying
    @Query("UPDATE MetodoPagamento m SET m.main = false WHERE m.utente.id = :userId AND m.id != :newMainMethodId AND m.main = true")
    void demoteAllOtherMainMethodsForUser(@Param("userId") UUID userId, @Param("newMainMethodId") UUID newMainMethodId);


     // Controlla se esiste almeno un metodo di pagamento per un dato utente
     // che sia contrassegnato come main.
    boolean existsByUtenteIdAndMain(UUID utenteId, boolean main);

    //Trova il primo metodo di pagamento creato da un utente
    // Utile per determinare un successore "main" quando il metodo principale viene eliminato o retrocesso.
    //Restituisce un Optional perché l'utente potrebbe non avere metodi di pagamento.
    Optional<MetodoPagamento> findFirstByUtente_Id(UUID utenteId);

}
