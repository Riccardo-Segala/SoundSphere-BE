--liquibase formatted sql

-- changeset riccardo:insert-initial-security-data-1
-- Commento: Inserimento dei permessi di base
SET search_path TO public;

INSERT INTO permesso (id, nome) VALUES
                                    ('5a4a9c6c-9477-4b71-92b0-8c20b8f416c1', 'ACQUISTO'),
                                    ('6b2b7e5e-3c5b-4d9f-a0c5-5a7c73c880a2', 'NOLEGGIO'),
                                    ('7c3c8d4d-2a6d-4c81-8b06-6b2d24b619e3', 'GESTIONE_STOCK');

---

-- changeset riccardo:insert-initial-security-data-2
-- Commento: Inserimento dei ruoli di base
INSERT INTO ruolo (id, nome) VALUES
                                 ('1e99d7a2-1d54-46e3-8f0a-6e5a0f5d72f4', 'UTENTE'),
                                 ('8a1c0d4f-5b7c-48b8-b4b3-9e9d6d5f47c5', 'DIPENDENTE'),
                                 ('9c2b1e5d-7a6e-4f7f-a2c6-0d1c7f4e9a8b', 'ORGANIZZATORE_EVENTI'),
-- Ho corretto l'UUID, che era l'unico non valido
                                 ('a3f2b1a1-3d7e-4c8a-b5c9-1f2a3c4b5e6f', 'ADMIN');

---

-- changeset riccardo:insert-initial-security-data-3
-- Commento: Associazione dei permessi ai ruoli
INSERT INTO ruolo_permesso (id_ruolo, id_permesso) VALUES
-- UTENTE: può solo acquistare
('1e99d7a2-1d54-46e3-8f0a-6e5a0f5d72f4', '5a4a9c6c-9477-4b71-92b0-8c20b8f416c1'),

-- DIPENDENTE: può solo gestire lo stock
('8a1c0d4f-5b7c-48b8-b4b3-9e9d6d5f47c5', '7c3c8d4d-2a6d-4c81-8b06-6b2d24b619e3'),

-- ORGANIZZATORE_EVENTI: può acquistare e noleggiare
('9c2b1e5d-7a6e-4f7f-a2c6-0d1c7f4e9a8b', '5a4a9c6c-9477-4b71-92b0-8c20b8f416c1'),
('9c2b1e5d-7a6e-4f7f-a2c6-0d1c7f4e9a8b', '6b2b7e5e-3c5b-4d9f-a0c5-5a7c73c880a2'),

-- ADMIN: ha tutti i permessi
('a3f2b1a1-3d7e-4c8a-b5c9-1f2a3c4b5e6f', '5a4a9c6c-9477-4b71-92b0-8c20b8f416c1'),
('a3f2b1a1-3d7e-4c8a-b5c9-1f2a3c4b5e6f', '6b2b7e5e-3c5b-4d9f-a0c5-5a7c73c880a2'),
('a3f2b1a1-3d7e-4c8a-b5c9-1f2a3c4b5e6f', '7c3c8d4d-2a6d-4c81-8b06-6b2d24b619e3');