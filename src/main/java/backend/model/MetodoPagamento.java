package backend.model;

import backend.model.enums.TipoPagamento;
import backend.model.enums.TipologiaIndirizzo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "metodo_pagamento")
public class MetodoPagamento {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    private String nomeSuCarta;
    private String numero;
    private String cvv;
    private LocalDate dataScadenza;
    private String paypalEmail;

    @Enumerated(EnumType.STRING)
    private TipoPagamento tipoPagamento;

    @Column(name = "is_main")
    private boolean main;

    @ManyToOne
    @JoinColumn(name = "id_utente")
    private Utente utente;

}