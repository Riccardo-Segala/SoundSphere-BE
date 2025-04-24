-- liquibase formatted sql

-- changeset riccardo:1745504870859-1
CREATE TABLE dipendente
(
    id                 UUID             NOT NULL,
    stipendio          DOUBLE PRECISION NOT NULL,
    scadenza_contratto date,
    data_assunzione    date,
    id_filiale         UUID             NOT NULL,
    CONSTRAINT pk_dipendente PRIMARY KEY (id)
);

-- changeset riccardo:1745504870859-2
CREATE TABLE filiale
(
    id UUID NOT NULL,
    CONSTRAINT pk_filiale PRIMARY KEY (id)
);

-- changeset riccardo:1745504870859-3
CREATE TABLE indirizzo
(
    id UUID NOT NULL,
    CONSTRAINT pk_indirizzo PRIMARY KEY (id)
);

-- changeset riccardo:1745504870859-4
CREATE TABLE organizzatore_eventi
(
    id            UUID NOT NULL,
    data_scadenza date,
    CONSTRAINT pk_organizzatoreeventi PRIMARY KEY (id)
);

-- changeset riccardo:1745504870859-5
CREATE TABLE prodotto
(
    id UUID NOT NULL,
    CONSTRAINT pk_prodotto PRIMARY KEY (id)
);

-- changeset riccardo:1745504870859-6
CREATE TABLE recensione
(
    id          UUID    NOT NULL,
    data        date    NOT NULL,
    num_stelle  INTEGER NOT NULL,
    id_prodotto UUID    NOT NULL,
    id_utente   UUID    NOT NULL,
    CONSTRAINT pk_recensione PRIMARY KEY (id)
);

-- changeset riccardo:1745504870859-7
CREATE TABLE utente
(
    id                 UUID         NOT NULL,
    nome               VARCHAR(255),
    cognome            VARCHAR(255),
    email              VARCHAR(255) NOT NULL,
    password           VARCHAR(255) NOT NULL,
    data_di_nascita    date,
    tipologia          VARCHAR(255),
    path_immagine      TEXT,
    sesso              VARCHAR(255),
    data_registrazione date,
    punti              INTEGER,
    id_vantaggio       UUID         NOT NULL,
    id_indirizzo       UUID         NOT NULL,
    CONSTRAINT pk_utente PRIMARY KEY (id)
);

-- changeset riccardo:1745504870859-8
CREATE TABLE vantaggio
(
    id UUID NOT NULL,
    CONSTRAINT pk_vantaggio PRIMARY KEY (id)
);

-- changeset riccardo:1745504870859-9
ALTER TABLE utente
    ADD CONSTRAINT uc_utente_email UNIQUE (email);

-- changeset riccardo:1745504870859-10
ALTER TABLE dipendente
    ADD CONSTRAINT FK_DIPENDENTE_ON_ID FOREIGN KEY (id) REFERENCES utente (id);

-- changeset riccardo:1745504870859-11
ALTER TABLE dipendente
    ADD CONSTRAINT FK_DIPENDENTE_ON_ID_FILIALE FOREIGN KEY (id_filiale) REFERENCES filiale (id);

-- changeset riccardo:1745504870859-12
ALTER TABLE organizzatore_eventi
    ADD CONSTRAINT FK_ORGANIZZATOREEVENTI_ON_ID FOREIGN KEY (id) REFERENCES utente (id);

-- changeset riccardo:1745504870859-13
ALTER TABLE recensione
    ADD CONSTRAINT FK_RECENSIONE_ON_ID_PRODOTTO FOREIGN KEY (id_prodotto) REFERENCES prodotto (id);

-- changeset riccardo:1745504870859-14
ALTER TABLE recensione
    ADD CONSTRAINT FK_RECENSIONE_ON_ID_UTENTE FOREIGN KEY (id_utente) REFERENCES utente (id);

-- changeset riccardo:1745504870859-15
ALTER TABLE utente
    ADD CONSTRAINT FK_UTENTE_ON_ID_INDIRIZZO FOREIGN KEY (id_indirizzo) REFERENCES indirizzo (id);

-- changeset riccardo:1745504870859-16
ALTER TABLE utente
    ADD CONSTRAINT FK_UTENTE_ON_ID_VANTAGGIO FOREIGN KEY (id_vantaggio) REFERENCES vantaggio (id);

