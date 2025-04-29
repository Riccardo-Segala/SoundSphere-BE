-- liquibase formatted sql

-- changeset Miriam:1745593721231-1
CREATE TABLE carrello
(
    quantita    INTEGER NOT NULL,
    wishlist    BOOLEAN NOT NULL,
    id_utente   UUID    NOT NULL,
    id_prodotto UUID    NOT NULL,
    CONSTRAINT pk_carrello PRIMARY KEY (id_utente, id_prodotto)
);

-- changeset Miriam:1745593721231-2
ALTER TABLE carrello
    ADD CONSTRAINT FK_CARRELLO_ON_ID_PRODOTTO FOREIGN KEY (id_prodotto) REFERENCES prodotto (id);

-- changeset Miriam:1745593721231-3
ALTER TABLE carrello
    ADD CONSTRAINT FK_CARRELLO_ON_ID_UTENTE FOREIGN KEY (id_utente) REFERENCES utente (id);

