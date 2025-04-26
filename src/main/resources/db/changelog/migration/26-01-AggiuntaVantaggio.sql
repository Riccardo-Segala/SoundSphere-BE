-- liquibase formatted sql

-- changeset Miriam:1745685380623-1
ALTER TABLE vantaggio
    ADD nome VARCHAR(255);
ALTER TABLE vantaggio
    ADD punteggio_massimo INTEGER;
ALTER TABLE vantaggio
    ADD punteggio_minimo INTEGER;
ALTER TABLE vantaggio
    ADD sconto DOUBLE PRECISION;

