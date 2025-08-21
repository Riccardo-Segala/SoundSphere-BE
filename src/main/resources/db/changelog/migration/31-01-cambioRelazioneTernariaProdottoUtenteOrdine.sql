-- liquibase formatted sql

-- changeset riccardo:1755703264836-11
ALTER TABLE dettagli_ordine
    DROP CONSTRAINT fk_dettagli_ordine_on_id_utente;

-- changeset riccardo:1755703264836-8
ALTER TABLE ordine
    ADD id_utente UUID;

-- changeset riccardo:1755703264836-9
ALTER TABLE ordine
    ALTER COLUMN id_utente SET NOT NULL;

-- changeset riccardo:1755703264836-10
ALTER TABLE ordine
    ADD CONSTRAINT FK_ORDINE_ON_ID_UTENTE FOREIGN KEY (id_utente) REFERENCES utente (id);

-- changeset riccardo:1755703264836-12
ALTER TABLE dettagli_ordine
    DROP COLUMN id_utente;



