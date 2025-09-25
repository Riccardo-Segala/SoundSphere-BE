-- liquibase formatted sql

-- changeset riccardo:1758809323842-4
INSERT INTO public.filiale (id,email,indirizzo,nome,telefono) VALUES
      ('c2f8c4e4-e7b3-4a1d-8a6c-0b1a2b3c4d5e'::uuid,'online@soundsphere.it','','OnlineStore','11223344556'),
      ('00463066-b672-49b6-b9e2-7032dd975c74'::uuid,'roma@soundsphere.it','Via del Corso, Roma, 00186, RM, Italia','Roma','3391122334'),
      ('ad4b4c33-8699-4961-b28d-c0bc40e97eb5'::uuid,'verona@soundsphere.it','Via Mazzini, Verona, 37121, VR, Italia','Verona','3391523748')
ON CONFLICT (id) DO NOTHING;


