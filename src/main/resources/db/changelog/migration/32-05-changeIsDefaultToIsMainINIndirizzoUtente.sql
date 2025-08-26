-- liquibase formatted sql

-- changeset riccardo:1756210658895-1
ALTER TABLE indirizzo_utente
    ADD is_main BOOLEAN;

-- changeset riccardo:1756210658895-2
ALTER TABLE indirizzo_utente
    DROP COLUMN is_default;

