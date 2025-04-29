-- liquibase formatted sql

-- changeset Miriam:1745686101959-1
CREATE TABLE dati_statici
(
    id     UUID             NOT NULL,
    nome   VARCHAR(255),
    valore DOUBLE PRECISION NOT NULL,
    CONSTRAINT pk_dati_statici PRIMARY KEY (id)
);

