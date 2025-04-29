-- liquibase formatted sql

-- changeset Miriam:1745596789630-1
ALTER TABLE prodotto
    ADD costo_giornaliero DOUBLE PRECISION;
ALTER TABLE prodotto
    ADD descrizione VARCHAR(255);
ALTER TABLE prodotto
    ADD is_rentable BOOLEAN;
ALTER TABLE prodotto
    ADD marca VARCHAR(255);
ALTER TABLE prodotto
    ADD nome VARCHAR(255);
ALTER TABLE prodotto
    ADD path_immagine TEXT;
ALTER TABLE prodotto
    ADD prezzo DOUBLE PRECISION;

-- changeset Miriam:1745596789630-8
ALTER TABLE prodotto
    ALTER COLUMN prezzo SET NOT NULL;

