-- liquibase formatted sql

-- changeset Miriam:1745595139069-1
ALTER TABLE filiale
    ADD email VARCHAR(255);
ALTER TABLE filiale
    ADD indirizzo VARCHAR(255);
ALTER TABLE filiale
    ADD nome VARCHAR(255);
ALTER TABLE filiale
    ADD telefono VARCHAR(255);

