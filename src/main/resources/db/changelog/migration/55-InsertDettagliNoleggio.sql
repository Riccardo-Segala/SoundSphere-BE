-- liquibase formatted sql

-- changeset riccardo:1758809323842-16
INSERT INTO public.dettagli_noleggio (quantita,id_noleggio,id_prodotto) VALUES
    (2,'c625e64e-14c0-49d6-8984-2a311aa735fa'::uuid,'a1b2c3d4-e5f6-4a7b-8c9d-1e2f3a4b5c6d'::uuid),
    (4,'1f72a966-2574-4b37-bdde-4ad5fe7119db'::uuid,'a1b2c3d4-e5f6-4a7b-8c9d-1e2f3a4b5c6d'::uuid),
    (1,'ca718d63-8681-4b6c-b5e3-019fb19c8554'::uuid,'34c09f57-fdb7-45e2-bfb4-27638165f7a0'::uuid),
    (1,'5df13b03-7db8-4ee0-bdbc-d68ebb9b5ac3'::uuid,'6b8a891d-7bb7-47b5-aede-2e7e70fae010'::uuid)
ON CONFLICT (id_noleggio,id_prodotto) DO NOTHING;


