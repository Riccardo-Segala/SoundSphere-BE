-- liquibase formatted sql

-- changeset riccardo:1755711451727-1
ALTER TABLE ordine
    ADD data_consegna_stimata date;

