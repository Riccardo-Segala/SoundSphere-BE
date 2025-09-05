package backend.security;

import backend.model.Utente;
import backend.model.UtenteRuolo;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CustomUserDetails implements UserDetails {

    @Getter
    private final UUID id;
    private final String username; // L'email dell'utente
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;
    private final boolean isEnabled;
    private final boolean isAccountNonLocked;

    /**
     * Costruisce l'oggetto UserDetails copiando i dati dall'entità Utente.
     * @param utente L'entità Utente recuperata dal repository.
     */
    public CustomUserDetails(Utente utente) {
        Objects.requireNonNull(utente, "L'entità Utente non può essere nulla.");

        this.id = utente.getId();
        this.username = utente.getEmail();
        this.password = utente.getPassword();

        // Mappa i ruoli dell'entità in una collezione di GrantedAuthority.
        // Il prefisso 'ROLE_' è una convenzione standard di Spring Security.
        this.authorities = utente.getUtenteRuoli().stream()
                .filter(ur -> ur.getDataScadenza() == null || ur.getDataScadenza().isAfter(LocalDate.now()))
                .map(UtenteRuolo::getRuolo)
                .flatMap(ruolo -> {
                    Stream<GrantedAuthority> ruoloStream = Stream.of(
                            new SimpleGrantedAuthority("ROLE_" + ruolo.getNome().toUpperCase())
                    );

                    Stream<GrantedAuthority> permessiStream = ruolo.getPermessi().stream()
                            .map(permesso -> new SimpleGrantedAuthority(permesso.getNome().toUpperCase()));

                    return Stream.concat(ruoloStream, permessiStream);
                })
                .collect(Collectors.toList());

        // Copia lo stato dell'account dall'entità
        this.isEnabled = utente.isEnabled();
        this.isAccountNonLocked = utente.isAccountNonLocked();
    }



    // --- Metodi richiesti dall'interfaccia UserDetails ---

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        // Valore fisso a 'true' perché non gestiamo la scadenza dell'account
        // in questo modello.
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // Restituisce il valore letto dall'entità Utente al momento della creazione.
        return this.isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // Valore fisso a 'true' perché non gestiamo la scadenza della password.
        return true;
    }

    @Override
    public boolean isEnabled() {
        // Restituisce il valore letto dall'entità Utente al momento della creazione.
        return this.isEnabled;
    }
}