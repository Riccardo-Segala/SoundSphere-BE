-- liquibase formatted sql

-- changeset riccardo:1758809323842-10
INSERT INTO public.metodo_pagamento (id,nome_su_carta,numero,cvv,data_scadenza,paypal_email,tipo_pagamento,id_utente,is_main) VALUES
    ('7e5f9c1a-2b3d-4e8f-9a0b-1c2d3e4f5a6b'::uuid,'Riccardo Segala','1111-2222-3333-4444','123','2050-10-10',NULL,'CARTA_DI_CREDITO','e2bd0a46-f13e-4b23-8119-9755b1089e13'::uuid,true)
ON CONFLICT (id) DO NOTHING;


