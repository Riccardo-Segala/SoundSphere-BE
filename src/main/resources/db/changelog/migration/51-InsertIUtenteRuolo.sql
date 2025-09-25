-- liquibase formatted sql

-- changeset riccardo:1758809323842-12
INSERT INTO public.utente_ruolo (id_utente,id_ruolo,id,data_scadenza) VALUES
      ('c945016e-fd15-480b-acc1-4ac71c8ffaec'::uuid,'8a1c0d4f-5b7c-48b8-b4b3-9e9d6d5f47c5'::uuid,'1b02b0a4-8461-4506-8b4e-9de1ce997e51'::uuid,NULL),
      ('24d53fd4-1685-4c48-b856-aad58eb2eb2e'::uuid,'1e99d7a2-1d54-46e3-8f0a-6e5a0f5d72f4'::uuid,'b1eea0ca-57a1-42cd-bc65-8a78439880ce'::uuid,NULL),
      ('1438fb78-94f9-4762-9e23-8e60e8b304aa'::uuid,'a3f2b1a1-3d7e-4c8a-b5c9-1f2a3c4b5e6f'::uuid,'487bbc9f-ce98-4e4a-9b5f-f5104bb83fda'::uuid,NULL),
      ('e2bd0a46-f13e-4b23-8119-9755b1089e13'::uuid,'9c2b1e5d-7a6e-4f7f-a2c6-0d1c7f4e9a8b'::uuid,'828610c9-c0e9-47c9-b8be-3829493520f1'::uuid,'2026-09-08'),
      ('e2bd0a46-f13e-4b23-8119-9755b1089e13'::uuid,'a3f2b1a1-3d7e-4c8a-b5c9-1f2a3c4b5e6f'::uuid,'c99fe0ae-fa48-4280-a5ae-cdc7cb7e0ab5'::uuid,NULL)
ON CONFLICT (id) DO NOTHING;


