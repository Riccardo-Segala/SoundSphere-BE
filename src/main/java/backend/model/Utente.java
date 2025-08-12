package backend.model;

import backend.model.enums.Sesso;
import backend.model.enums.Tipologia;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "utente")
@Inheritance(strategy = InheritanceType.JOINED) // Strategia JOINED (tabelle separate per le sotto-classi)
@DiscriminatorValue("UTENTE")
@DiscriminatorColumn(name = "tipologia", discriminatorType = DiscriminatorType.STRING) // Nome della colonna discriminante
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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_vantaggio")
    private Vantaggio vantaggio;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
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