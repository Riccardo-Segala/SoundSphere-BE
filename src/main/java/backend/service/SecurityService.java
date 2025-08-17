// in backend.service.SecurityService.java
package backend.service;

import backend.model.Utente;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Questo servizio contiene logiche di sicurezza personalizzate che possono essere
 * invocate dalle annotazioni @PreAuthorize nei controller.
 */
@Service("securityService") // Il nome del servizio è specificato per essere utilizzato nelle annotazioni @PreAuthorize
public class SecurityService {
    /**
     * Controlla se l'utente autenticato è il proprietario dell'oggetto con l'ID specificato nell'URL.
     *
     * @param authentication L'oggetto Authentication che rappresenta l'utente autenticato.
     * @param idFromUrl      L'ID dell'oggetto da confrontare con l'ID dell'utente autenticato.
     * @return true se l'utente autenticato è il proprietario, false altrimenti.
     */
    public boolean isOwner(Authentication authentication, UUID idFromUrl) {
        // Controllo di sicurezza base
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        // Estrae l'oggetto Utente che il JwtAuthenticationFilter ha caricato
        Utente authenticatedUser = (Utente) authentication.getPrincipal();

        // Estrae il suo ID
        UUID idFromToken = authenticatedUser.getId();

        // Confronta l'ID dell'utente autenticato con quello richiesto nell'URL
        return idFromToken.equals(idFromUrl);
    }
}