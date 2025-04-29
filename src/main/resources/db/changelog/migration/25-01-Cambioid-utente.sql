-- liquibase formatted sql

-- changeset Miriam:1745588953366-4
ALTER TABLE indirizzo_utente DROP CONSTRAINT fk_indirizzo_utente_on_utente;

-- changeset Miriam:1745588953366-1
ALTER TABLE indirizzo_utente
    ADD id_utente UUID;

-- changeset Miriam:1745588953366-2
ALTER TABLE indirizzo_utente
    ALTER COLUMN id_utente SET NOT NULL;

-- changeset Miriam:1745588953366-3
ALTER TABLE indirizzo_utente
    ADD CONSTRAINT FK_INDIRIZZO_UTENTE_ON_ID_UTENTE FOREIGN KEY (id_utente) REFERENCES utente (id);

-- changeset Miriam:1745588953366-5
ALTER TABLE indirizzo_utente DROP COLUMN utente_id;

