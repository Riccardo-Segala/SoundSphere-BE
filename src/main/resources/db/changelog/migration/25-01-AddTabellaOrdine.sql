-- liquibase formatted sql

-- changeset Miriam:1745592157413-1
CREATE TABLE ordine
(
    id                  UUID             NOT NULL,
    data_acquisto       date,
    data_consegna       date,
    spedizione_gratuita BOOLEAN,
    totale              DOUBLE PRECISION NOT NULL,
    stato               VARCHAR(255),
    CONSTRAINT pk_ordine PRIMARY KEY (id)
);

