-- liquibase formatted sql

-- changeset riccardo:1745742120413-1
CREATE TABLE dettagli_ordine
(
    id_utente   UUID,
    quantita    INTEGER NOT NULL,
    id_ordine   UUID    NOT NULL,
    id_prodotto UUID    NOT NULL,
    CONSTRAINT pk_dettagli_ordine PRIMARY KEY (id_ordine, id_prodotto)
);

-- changeset riccardo:1745742120413-2
CREATE TABLE stock
(
    quantita              INTEGER NOT NULL,
    quantita_per_noleggio INTEGER NOT NULL,
    id_filiale            UUID    NOT NULL,
    id_prodotto           UUID    NOT NULL,
    CONSTRAINT pk_stock PRIMARY KEY (id_filiale, id_prodotto)
);

-- changeset riccardo:1745742120413-3
ALTER TABLE dettagli_ordine
    ADD CONSTRAINT FK_DETTAGLI_ORDINE_ON_ID_ORDINE FOREIGN KEY (id_ordine) REFERENCES ordine (id);

-- changeset riccardo:1745742120413-4
ALTER TABLE dettagli_ordine
    ADD CONSTRAINT FK_DETTAGLI_ORDINE_ON_ID_PRODOTTO FOREIGN KEY (id_prodotto) REFERENCES prodotto (id);

-- changeset riccardo:1745742120413-5
ALTER TABLE dettagli_ordine
    ADD CONSTRAINT FK_DETTAGLI_ORDINE_ON_ID_UTENTE FOREIGN KEY (id_utente) REFERENCES utente (id);

-- changeset riccardo:1745742120413-6
ALTER TABLE stock
    ADD CONSTRAINT FK_STOCK_ON_ID_FILIALE FOREIGN KEY (id_filiale) REFERENCES ordine (id);

-- changeset riccardo:1745742120413-7
ALTER TABLE stock
    ADD CONSTRAINT FK_STOCK_ON_ID_PRODOTTO FOREIGN KEY (id_prodotto) REFERENCES prodotto (id);

