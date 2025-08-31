-- liquibase formatted sql

-- changeset riccardo:1756652157436-1
ALTER TABLE metodo_pagamento
    ADD is_main BOOLEAN;

-- changeset riccardo:1756652157436-2
ALTER TABLE metodo_pagamento
    DROP COLUMN is_default;

