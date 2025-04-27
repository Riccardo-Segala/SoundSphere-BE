-- liquibase formatted sql

-- changeset Miriam:1745696985403-1
CREATE TABLE metodo_pagamento
(
    id             UUID NOT NULL,
    nome_su_carta  VARCHAR(255),
    numero         VARCHAR(255),
    cvv            VARCHAR(255),
    data_scadenza  date,
    paypal_email   VARCHAR(255),
    tipo_pagamento VARCHAR(255),
    is_default     BOOLEAN,
    id_utente      UUID,
    CONSTRAINT pk_metodo_pagamento PRIMARY KEY (id)
);

-- changeset Miriam:1745696985403-2
ALTER TABLE metodo_pagamento
    ADD CONSTRAINT FK_METODO_PAGAMENTO_ON_ID_UTENTE FOREIGN KEY (id_utente) REFERENCES utente (id);

