--liquibase formatted sql

-- changeset riccardo:1755007724344-1
-- Commento: Creazione della tabella 'permesso'
CREATE TABLE permesso (
                          id UUID NOT NULL,
                          nome VARCHAR(255) NOT NULL,
                          CONSTRAINT pk_permesso PRIMARY KEY (id)
);

-- changeset riccardo:1755007724344-2
-- Commento: Creazione della tabella 'ruolo'
CREATE TABLE ruolo (
                       id UUID NOT NULL,
                       nome VARCHAR(255) NOT NULL,
                       CONSTRAINT pk_ruolo PRIMARY KEY (id)
);

-- changeset riccardo:1755007724344-3
-- Commento: Creazione della tabella di join 'utente_ruolo'
CREATE TABLE utente_ruolo (
                              id_utente UUID NOT NULL,
                              id_ruolo UUID NOT NULL,
                              CONSTRAINT pk_utente_ruolo PRIMARY KEY (id_utente, id_ruolo)
);

-- changeset riccardo:1755007724344-4
-- Commento: Creazione della tabella di join 'ruolo_permesso'
CREATE TABLE ruolo_permesso (
                                id_ruolo UUID NOT NULL,
                                id_permesso UUID NOT NULL,
                                CONSTRAINT pk_ruolo_permesso PRIMARY KEY (id_ruolo, id_permesso)
);

-- changeset riccardo:1755007724344-5
-- Commento: Aggiunta dei vincoli di unicità per i nomi
ALTER TABLE permesso ADD CONSTRAINT uc_permesso_nome UNIQUE (nome);
ALTER TABLE ruolo ADD CONSTRAINT uc_ruolo_nome UNIQUE (nome);

-- changeset riccardo:1755007724344-6
-- Commento: Aggiunta delle chiavi esterne per la tabella 'utente_ruolo'
ALTER TABLE utente_ruolo
    ADD CONSTRAINT fk_utru_on_utente
        FOREIGN KEY (id_utente) REFERENCES utente (id)
            ON DELETE CASCADE;

ALTER TABLE utente_ruolo
    ADD CONSTRAINT fk_utru_on_ruolo
        FOREIGN KEY (id_ruolo) REFERENCES ruolo (id)
            ON DELETE CASCADE;

-- changeset riccardo:1755007724344-7
-- Commento: Aggiunta delle chiavi esterne per la tabella 'ruolo_permesso'
ALTER TABLE ruolo_permesso
    ADD CONSTRAINT fk_ruper_on_ruolo
        FOREIGN KEY (id_ruolo) REFERENCES ruolo (id)
            ON DELETE CASCADE;

ALTER TABLE ruolo_permesso
    ADD CONSTRAINT fk_ruper_on_permesso
        FOREIGN KEY (id_permesso) REFERENCES permesso (id)
            ON DELETE CASCADE;