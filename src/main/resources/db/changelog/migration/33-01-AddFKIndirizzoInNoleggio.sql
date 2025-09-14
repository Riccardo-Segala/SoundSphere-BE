-- liquibase formatted sql

-- changeset Miriam:1757879562999-1
ALTER TABLE noleggio
    ADD id_indirizzo_utente UUID;

-- changeset Miriam:1757879562999-2
ALTER TABLE noleggio
    ADD CONSTRAINT FK_NOLEGGIO_ON_ID_INDIRIZZO_UTENTE FOREIGN KEY (id_indirizzo_utente) REFERENCES indirizzo_utente (id);

