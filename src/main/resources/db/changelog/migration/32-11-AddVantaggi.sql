-- liquibase formatted sql

-- changeset riccardo:1756730264688-1
INSERT INTO vantaggio (id, nome, punteggio_massimo, punteggio_minimo, sconto) VALUES
(gen_random_uuid(), 'Starter', 100, 0, 0),
(gen_random_uuid(), 'Explorer', 200, 101, 5),
(gen_random_uuid(), 'Adventurer', 500, 201, 10),
(gen_random_uuid(), 'Pioneer', 1000, 501, 15),
(gen_random_uuid(), 'Expert', 2500, 1001, 20),
(gen_random_uuid(), 'Master', 5000, 2501, 30),
(gen_random_uuid(), 'Guru', 7000, 5001, 40),
(gen_random_uuid(), 'Legend', 2147483647, 7001, 50);

