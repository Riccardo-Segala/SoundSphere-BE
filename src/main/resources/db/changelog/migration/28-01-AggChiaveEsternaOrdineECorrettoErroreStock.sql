-- liquibase formatted sql

-- changeset Miriam:1745855998117-4
ALTER TABLE stock
    DROP CONSTRAINT fk_stock_on_id_filiale;

-- changeset Miriam:1745855998117-1
ALTER TABLE ordine
    ADD id_indirizzo_utente UUID;

-- changeset Miriam:1745855998117-2
ALTER TABLE ordine
    ADD CONSTRAINT FK_ORDINE_ON_ID_INDIRIZZO_UTENTE FOREIGN KEY (id_indirizzo_utente) REFERENCES indirizzo_utente (id);

-- changeset Miriam:1745855998117-3
ALTER TABLE stock
    ADD CONSTRAINT FK_STOCK_ON_ID_FILIALE FOREIGN KEY (id_filiale) REFERENCES filiale (id);

