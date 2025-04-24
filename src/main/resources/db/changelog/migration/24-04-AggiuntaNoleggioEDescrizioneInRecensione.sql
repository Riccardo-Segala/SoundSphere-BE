-- liquibase formatted sql

-- changeset riccardo:1745505709426-1
CREATE TABLE noleggio
(
    id                UUID             NOT NULL,
    data_inizio       date,
    data_scadenza     date,
    data_restituzione date,
    data_pagamento    date,
    totale            DOUBLE PRECISION NOT NULL,
    CONSTRAINT pk_noleggio PRIMARY KEY (id)
);

-- changeset riccardo:1745505709426-2
ALTER TABLE recensione
    ADD descrizione TEXT;

-- changeset riccardo:1745505709426-3
ALTER TABLE recensione
    ALTER COLUMN descrizione SET NOT NULL;

