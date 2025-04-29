package backend.model;

import backend.model.enums.StatoOrdine;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "ordine")
public class Ordine {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;


    private LocalDate dataAcquisto;
    private LocalDate dataConsegna;

    @Column(name = "spedizione_gratuita")
    private boolean spedizioneGratuita;

    private double totale;

    @Enumerated(EnumType.STRING)
    private StatoOrdine stato;

    @ManyToOne
    @JoinColumn(name = "id_indirizzo_utente")
    private IndirizzoUtente indirizzo;


}