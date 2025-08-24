-- liquibase formatted sql

-- changeset riccardo:1756027304453-1
CREATE TABLE categoria
(
    id        UUID         NOT NULL,
    nome      VARCHAR(255) NOT NULL,
    slug      VARCHAR(255) NOT NULL,
    parent_id UUID,
    CONSTRAINT pk_categoria PRIMARY KEY (id)
);

-- changeset riccardo:1756027304453-2
CREATE TABLE prodotto_categoria
(
    categoria_id UUID NOT NULL,
    prodotto_id  UUID NOT NULL,
    CONSTRAINT pk_prodotto_categoria PRIMARY KEY (categoria_id, prodotto_id)
);

-- changeset riccardo:1756027304453-3
ALTER TABLE categoria
    ADD CONSTRAINT uc_categoria_slug UNIQUE (slug);

-- changeset riccardo:1756027304453-4
ALTER TABLE categoria
    ADD CONSTRAINT FK_CATEGORIA_ON_PARENT FOREIGN KEY (parent_id) REFERENCES categoria (id);

-- changeset riccardo:1756027304453-5
ALTER TABLE prodotto_categoria
    ADD CONSTRAINT fk_procat_on_categoria FOREIGN KEY (categoria_id) REFERENCES categoria (id);

-- changeset riccardo:1756027304453-6
ALTER TABLE prodotto_categoria
    ADD CONSTRAINT fk_procat_on_prodotto FOREIGN KEY (prodotto_id) REFERENCES prodotto (id);

