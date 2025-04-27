-- liquibase formatted sql

-- changeset riccardo:1745683657389-1
ALTER TABLE utente
    DROP CONSTRAINT fk_utente_on_id_indirizzo_utente;

-- changeset riccardo:1745683657389-2
ALTER TABLE utente
    DROP COLUMN id_indirizzo_utente;

