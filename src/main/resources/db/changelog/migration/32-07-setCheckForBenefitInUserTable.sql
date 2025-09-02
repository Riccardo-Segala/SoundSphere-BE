-- liquibase formatted sql

-- changeset riccardo:1756210658895-1
ALTER TABLE utente
    ADD CONSTRAINT chk_utente_vantaggio CHECK (tipologia <> 'UTENTE' OR id_vantaggio IS NOT NULL);