package backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "noleggio")
public class Noleggio {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    private LocalDate dataInizio;

    private LocalDate dataScadenza;

    private LocalDate dataRestituzione;

    private LocalDate dataPagamento;

    private double totale;

}