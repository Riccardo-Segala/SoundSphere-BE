-- liquibase formatted sql

-- changeset riccardo:1758809323842-8
INSERT INTO public.utente (id,nome,cognome,email,"password",data_di_nascita,tipologia,path_immagine,sesso,data_registrazione,punti,id_vantaggio) VALUES
     ('1438fb78-94f9-4762-9e23-8e60e8b304aa'::uuid,'Admin','Admin','admin@soundsphere.it','$2a$10$AZnayv5HAT1DNGlALfyVKuIrd8vnTEWjhqjA/bmT2LWgsK3yO096u','1963-02-25','UTENTE',NULL,'NON_SPECIFICATO','2025-09-25',0,'09412801-bcfa-4bfa-ad55-4aec880525dc'::uuid),
     ('e2bd0a46-f13e-4b23-8119-9755b1089e13'::uuid,'Riccardo','Segala','riccardosegala99@gmail.com','$2a$10$yZX3GKVORGliOa3C0hy61eSb4Gv4yOe4iQjNkzi7DiTuQLkQAra8e','1999-10-10','UTENTE','','MASCHIO','2025-08-14',1166,'ff7d695a-21a0-490f-82ec-74cbf872dbfc'::uuid),
     ('c945016e-fd15-480b-acc1-4ac71c8ffaec'::uuid,'Mario ','Rossi','mario.rossi@soundsphere.it','$2a$10$QMBm.Lrw0wLAaKBtKX.tKOIj5hRRyBK6fi6RNFo0yEGEwO36vGNiC','1978-02-25','DIPENDENTE',NULL,'MASCHIO','2025-09-25',NULL,NULL),
     ('24d53fd4-1685-4c48-b856-aad58eb2eb2e'::uuid,'mario','rossi','mario.rossi@gmail.com','$2a$10$homOHA84I4/7hy.sbd06deeIQd9PHYOWHwfauAqpcv/Rck9hKstA.','1992-01-25','UTENTE',NULL,'MASCHIO','2025-09-25',0,'09412801-bcfa-4bfa-ad55-4aec880525dc'::uuid)
ON CONFLICT (id) DO NOTHING;


