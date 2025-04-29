-- liquibase formatted sql

-- changeset Miriam:1745588194331-6
ALTER TABLE utente DROP CONSTRAINT fk_utente_on_id_indirizzo;

-- changeset Miriam:1745588194331-1
CREATE TABLE indirizzo_utente
(
    id         UUID NOT NULL,
    via        VARCHAR(255),
    civico     VARCHAR(255),
    cap        VARCHAR(255),
    citta      VARCHAR(255),
    provincia  VARCHAR(255),
    nazione    VARCHAR(255),
    is_default BOOLEAN,
    tipologia  VARCHAR(255),
    utente_id  UUID NOT NULL,
    CONSTRAINT pk_indirizzo_utente PRIMARY KEY (id)
);

-- changeset Miriam:1745588194331-2
ALTER TABLE utente
    ADD id_indirizzo_utente UUID;

-- changeset Miriam:1745588194331-3
ALTER TABLE utente
    ALTER COLUMN id_indirizzo_utente SET NOT NULL;

-- changeset Miriam:1745588194331-4
ALTER TABLE indirizzo_utente
    ADD CONSTRAINT FK_INDIRIZZO_UTENTE_ON_UTENTE FOREIGN KEY (utente_id) REFERENCES utente (id);

-- changeset Miriam:1745588194331-5
ALTER TABLE utente
    ADD CONSTRAINT FK_UTENTE_ON_ID_INDIRIZZO_UTENTE FOREIGN KEY (id_indirizzo_utente) REFERENCES indirizzo_utente (id);

-- changeset Miriam:1745588194331-7
DROP TABLE indirizzo CASCADE;

-- changeset Miriam:1745588194331-8
ALTER TABLE utente DROP COLUMN id_indirizzo;

