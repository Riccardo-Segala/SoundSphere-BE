-- liquibase formatted sql

-- changeset riccardo:1756719899902-5
ALTER TABLE dettagli_noleggio
    DROP CONSTRAINT fk_dettagli_noleggio_on_id_organizzatore_eventi;

-- changeset riccardo:1756719899902-6
ALTER TABLE organizzatore_eventi
    DROP CONSTRAINT fk_organizzatoreeventi_on_id;

-- changeset riccardo:1756719899902-1
ALTER TABLE noleggio
    ADD id_utente UUID;

-- changeset riccardo:1756719899902-2
ALTER TABLE noleggio
    ALTER COLUMN id_utente SET NOT NULL;

-- changeset riccardo:1756719899902-4
ALTER TABLE noleggio
    ADD CONSTRAINT FK_NOLEGGIO_ON_ID_UTENTE FOREIGN KEY (id_utente) REFERENCES utente (id);

-- changeset riccardo:1756719899902-9
ALTER TABLE dettagli_noleggio
    DROP COLUMN id_organizzatore_eventi;

-- changeset riccardo:1756719899902-8
DROP TABLE organizzatore_eventi;