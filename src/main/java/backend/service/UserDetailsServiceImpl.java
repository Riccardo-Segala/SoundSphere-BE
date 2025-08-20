package backend.service;

import backend.model.Utente;
import backend.repository.UtenteRepository;
import backend.security.CustomUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Servizio che implementa l'interfaccia UserDetailsService per integrare
 * l'autenticazione di Spring Security con il modello dati dell'applicazione.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UtenteRepository utenteRepository;

    /**
     * Costruttore per l'iniezione delle dipendenze del repository Utente.
     * @param utenteRepository Il repository per accedere ai dati degli utenti.
     */
    public UserDetailsServiceImpl(UtenteRepository utenteRepository) {
        this.utenteRepository = utenteRepository;
    }

    /**
     * Metodo chiamato da Spring Security per recuperare un utente tramite il suo username (email).
     * @param username L'email dell'utente da cercare.
     * @return un'istanza di CustomUserDetails se l'utente viene trovato.
     * @throws UsernameNotFoundException se nessun utente corrisponde all'email fornita.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Cerca l'entità Utente nel database usando l'email come identificativo.
        Utente utente = utenteRepository.findByEmailWithRuoli(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("Nessun utente trovato con l'email: " + username));

        // Crea e restituisce l'oggetto CustomUserDetails, che Spring Security
        // userà per il processo di autenticazione.
        return new CustomUserDetails(utente);
    }
}