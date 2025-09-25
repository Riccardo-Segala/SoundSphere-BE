-- liquibase formatted sql

-- changeset riccardo:1758809323842-15
INSERT INTO public.dettagli_ordine (quantita,id_ordine,id_prodotto) VALUES
    (2,'1bde045d-7659-408f-8773-d2ee2544a89b'::uuid,'e8b9b4a0-c8f4-4e9a-9b1a-2b3c4d5e6f7a'::uuid),
    (1,'1bde045d-7659-408f-8773-d2ee2544a89b'::uuid,'f3d1a2b3-e4f5-4a6b-8c9d-0e1f2a3b4c5d'::uuid),
    (2,'e5774dcf-addc-4abb-9aea-f24143b0390e'::uuid,'e8b9b4a0-c8f4-4e9a-9b1a-2b3c4d5e6f7a'::uuid),
    (1,'e5774dcf-addc-4abb-9aea-f24143b0390e'::uuid,'f3d1a2b3-e4f5-4a6b-8c9d-0e1f2a3b4c5d'::uuid),
    (2,'2aea89ed-2647-46d2-8d2a-a7bc233fe121'::uuid,'f3d1a2b3-e4f5-4a6b-8c9d-0e1f2a3b4c5d'::uuid),
    (2,'2aea89ed-2647-46d2-8d2a-a7bc233fe121'::uuid,'e8b9b4a0-c8f4-4e9a-9b1a-2b3c4d5e6f7a'::uuid),
    (2,'2aea89ed-2647-46d2-8d2a-a7bc233fe121'::uuid,'a1b2c3d4-e5f6-4a7b-8c9d-1e2f3a4b5c6d'::uuid),
    (1,'32d9c4db-81bb-4b0b-8569-54c47fd3d7c6'::uuid,'a1b2c3d4-e5f6-4a7b-8c9d-1e2f3a4b5c6d'::uuid),
    (1,'52b8869f-1d0e-4d9c-8db1-6322b91270be'::uuid,'a1b2c3d4-e5f6-4a7b-8c9d-1e2f3a4b5c6d'::uuid)
ON CONFLICT (id_ordine,id_prodotto) DO NOTHING;


