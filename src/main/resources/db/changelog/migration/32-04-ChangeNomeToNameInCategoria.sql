-- liquibase formatted sql

-- changeset riccardo:1756050658058-1
ALTER TABLE categoria
    ADD name VARCHAR(255);

-- changeset riccardo:1756050658058-2
ALTER TABLE categoria
    ALTER COLUMN name SET NOT NULL;

-- changeset riccardo:1756050658058-3
ALTER TABLE categoria
    DROP COLUMN nome;

