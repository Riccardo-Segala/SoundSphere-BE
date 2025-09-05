-- liquibase formatted sql

-- changeset riccardo:1756730264688-1
ALTER TABLE utente_ruolo
    ADD data_scadenza date;

