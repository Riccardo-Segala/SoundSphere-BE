-- liquibase formatted sql

-- changeset riccardo:1755952803402-1
ALTER TABLE recensione
    ADD titolo VARCHAR(255);

-- changeset riccardo:1755952803402-2
ALTER TABLE recensione
    ALTER COLUMN titolo SET NOT NULL;

