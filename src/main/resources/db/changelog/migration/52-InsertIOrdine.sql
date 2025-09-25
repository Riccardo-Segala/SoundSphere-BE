-- liquibase formatted sql

-- changeset riccardo:1758809323842-13
INSERT INTO public.ordine (id,data_acquisto,data_consegna,spedizione_gratuita,totale,stato,id_indirizzo_utente,id_utente,data_consegna_stimata) VALUES
    ('1bde045d-7659-408f-8773-d2ee2544a89b'::uuid,'2025-08-20',NULL,false,1690.0,'IN_ATTESA','a1b9e8f0-6c7d-4e5f-8a9b-0c1d2e3f4a5b'::uuid,'e2bd0a46-f13e-4b23-8119-9755b1089e13'::uuid,'2025-08-23'),
    ('e5774dcf-addc-4abb-9aea-f24143b0390e'::uuid,'2025-08-20',NULL,false,1690.0,'IN_ATTESA','a1b9e8f0-6c7d-4e5f-8a9b-0c1d2e3f4a5b'::uuid,'e2bd0a46-f13e-4b23-8119-9755b1089e13'::uuid,'2025-08-23'),
    ('2aea89ed-2647-46d2-8d2a-a7bc233fe121'::uuid,'2025-09-15',NULL,false,10425.0,'IN_ATTESA','a1b9e8f0-6c7d-4e5f-8a9b-0c1d2e3f4a5b'::uuid,'e2bd0a46-f13e-4b23-8119-9755b1089e13'::uuid,'2025-09-18'),
    ('32d9c4db-81bb-4b0b-8569-54c47fd3d7c6'::uuid,'2025-09-21',NULL,true,4995.0,'IN_ATTESA','a1b9e8f0-6c7d-4e5f-8a9b-0c1d2e3f4a5b'::uuid,'e2bd0a46-f13e-4b23-8119-9755b1089e13'::uuid,'2025-09-24'),
    ('52b8869f-1d0e-4d9c-8db1-6322b91270be'::uuid,'2025-09-21',NULL,true,4985.0,'IN_ATTESA','a1b9e8f0-6c7d-4e5f-8a9b-0c1d2e3f4a5b'::uuid,'e2bd0a46-f13e-4b23-8119-9755b1089e13'::uuid,'2025-09-24')
ON CONFLICT (id) DO NOTHING;


