-- liquibase formatted sql

-- changeset riccardo:1-enable-uuid-extension
-- Prerequisito: Assicura che l'estensione per generare UUID sia attiva in PostgreSQL
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- changeset riccardo:2-drop-old-pk
ALTER TABLE utente_ruolo DROP CONSTRAINT IF EXISTS pk_utente_ruolo;

-- changeset riccardo:3-add-uuid-column
ALTER TABLE utente_ruolo ADD COLUMN IF NOT EXISTS id UUID;

-- changeset riccardo:4-populate-uuid-for-existing-rows
UPDATE utente_ruolo SET id = gen_random_uuid() WHERE id IS NULL;

-- changeset riccardo:5-add-not-null-and-pk-constraint
ALTER TABLE utente_ruolo ALTER COLUMN id SET NOT NULL;
ALTER TABLE utente_ruolo ADD CONSTRAINT pk_utente_ruolo PRIMARY KEY (id);
