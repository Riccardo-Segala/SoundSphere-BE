-- liquibase formatted sql

-- changeset riccardo:1758809323842-14
INSERT INTO public.noleggio (id,data_inizio,data_scadenza,data_restituzione,data_pagamento,totale,id_utente,id_indirizzo_utente) VALUES
     ('c625e64e-14c0-49d6-8984-2a311aa735fa'::uuid,'2025-09-24','2025-09-24',NULL,'2025-09-24',600.0,'e2bd0a46-f13e-4b23-8119-9755b1089e13'::uuid,'a1b9e8f0-6c7d-4e5f-8a9b-0c1d2e3f4a5b'::uuid),
     ('1f72a966-2574-4b37-bdde-4ad5fe7119db'::uuid,'2025-09-24','2025-09-30',NULL,'2025-09-24',7200.0,'e2bd0a46-f13e-4b23-8119-9755b1089e13'::uuid,'a1b9e8f0-6c7d-4e5f-8a9b-0c1d2e3f4a5b'::uuid),
     ('ca718d63-8681-4b6c-b5e3-019fb19c8554'::uuid,'2025-09-25','2025-09-25',NULL,'2025-09-25',200.0,'e2bd0a46-f13e-4b23-8119-9755b1089e13'::uuid,'a1b9e8f0-6c7d-4e5f-8a9b-0c1d2e3f4a5b'::uuid),
     ('5df13b03-7db8-4ee0-bdbc-d68ebb9b5ac3'::uuid,'2025-09-25','2025-09-25',NULL,'2025-09-25',100.0,'e2bd0a46-f13e-4b23-8119-9755b1089e13'::uuid,'a1b9e8f0-6c7d-4e5f-8a9b-0c1d2e3f4a5b'::uuid)
ON CONFLICT (id) DO NOTHING;


