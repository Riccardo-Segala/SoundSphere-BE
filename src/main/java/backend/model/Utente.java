package backend.model;

import backend.model.enums.Sesso;
import backend.model.enums.Tipologia;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Check;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.*;

@Getter
@Setter
@Entity
@Table(name = "utente")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorValue("UTENTE")
@DiscriminatorColumn(name = "tipologia", discriminatorType = DiscriminatorType.STRING)
@Check(constraints = "(tipologia <> 'UTENTE' OR id_vantaggio IS NOT NULL)")
public class Utente implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    private String nome;

    private String cognome;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private LocalDate dataDiNascita;

    @Column(name = "tipologia", insertable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    private Tipologia tipologia;

    @Column(columnDefinition = "TEXT")
    private String pathImmagine;

    @Enumerated(EnumType.STRING)
    private Sesso sesso;

    private LocalDate dataRegistrazione;

    private Integer punti;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_vantaggio")
    private Vantaggio vantaggio;

    @OneToMany(mappedBy = "utente", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<UtenteRuolo> utenteRuoli = new HashSet<>();

    @OneToMany(mappedBy = "utente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Ordine> ordini = new ArrayList<>();

    @OneToMany(mappedBy = "utente", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Noleggio> noleggi = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        for (UtenteRuolo ur : this.utenteRuoli) {
            // Controlla che il ruolo non sia scaduto
            if (ur.getDataScadenza() == null || ur.getDataScadenza().isAfter(LocalDate.now())) {
                Ruolo ruolo = ur.getRuolo();
                authorities.add(new SimpleGrantedAuthority("ROLE_" + ruolo.getNome()));
                for (Permesso permesso : ruolo.getPermessi()) {
                    authorities.add(new SimpleGrantedAuthority(permesso.getNome()));
                }
            }
        }
        return authorities;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}