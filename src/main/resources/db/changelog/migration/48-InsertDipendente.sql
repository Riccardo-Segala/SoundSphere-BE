-- liquibase formatted sql

-- changeset riccardo:1758809323842-9
INSERT INTO public.dipendente (id,stipendio,scadenza_contratto,data_assunzione,id_filiale) VALUES
    ('c945016e-fd15-480b-acc1-4ac71c8ffaec'::uuid,40000.0,'2025-09-30','2024-05-25','ad4b4c33-8699-4961-b28d-c0bc40e97eb5'::uuid)
ON CONFLICT (id) DO NOTHING;


