-- liquibase formatted sql

-- changeset riccardo:1745510414198-1
CREATE TABLE dettagli_noleggio
(
    id_organizzatore_eventi UUID    NOT NULL,
    quantita                INTEGER NOT NULL,
    id_noleggio             UUID    NOT NULL,
    id_prodotto             UUID    NOT NULL,
    CONSTRAINT pk_dettagli_noleggio PRIMARY KEY (id_noleggio, id_prodotto)
);

-- changeset riccardo:1745510414198-2
ALTER TABLE dettagli_noleggio
    ADD CONSTRAINT uc_dettagli_noleggio_id_organizzatore_eventi UNIQUE (id_organizzatore_eventi);

-- changeset riccardo:1745510414198-3
ALTER TABLE dettagli_noleggio
    ADD CONSTRAINT FK_DETTAGLI_NOLEGGIO_ON_ID_NOLEGGIO FOREIGN KEY (id_noleggio) REFERENCES noleggio (id);

-- changeset riccardo:1745510414198-4
ALTER TABLE dettagli_noleggio
    ADD CONSTRAINT FK_DETTAGLI_NOLEGGIO_ON_ID_ORGANIZZATORE_EVENTI FOREIGN KEY (id_organizzatore_eventi) REFERENCES organizzatore_eventi (id);

-- changeset riccardo:1745510414198-5
ALTER TABLE dettagli_noleggio
    ADD CONSTRAINT FK_DETTAGLI_NOLEGGIO_ON_ID_PRODOTTO FOREIGN KEY (id_prodotto) REFERENCES prodotto (id);

