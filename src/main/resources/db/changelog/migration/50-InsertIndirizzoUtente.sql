-- liquibase formatted sql

-- changeset riccardo:1758809323842-11
INSERT INTO public.indirizzo_utente (id,via,civico,cap,citta,provincia,nazione,tipologia,id_utente,is_main) VALUES
    ('a1b9e8f0-6c7d-4e5f-8a9b-0c1d2e3f4a5b'::uuid,'Oberdan','4','37045','Legnago','VR','Italia','SPEDIZIONE','e2bd0a46-f13e-4b23-8119-9755b1089e13'::uuid,true)
ON CONFLICT (id) DO NOTHING;


