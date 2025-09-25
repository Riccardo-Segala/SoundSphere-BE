-- liquibase formatted sql

-- changeset riccardo:1758809323842-5
INSERT INTO public.categoria (id, slug, parent_id, "name") VALUES
       ('a5752b56-fee9-42bd-9bb1-e7ac0b7af9b9'::uuid, 'chitarre', NULL, 'Chitarre'),
       ('e23e74ee-abf3-4ce0-b955-c81dc9b88f52'::uuid, 'acustiche', 'a5752b56-fee9-42bd-9bb1-e7ac0b7af9b9'::uuid, 'Acustiche'),
       ('400cdcd7-ccf7-46b6-9a48-9e518d2ca6ca'::uuid, 'batterie', NULL, 'Batterie'),
       ('a5a56e70-c609-42f2-90ce-5d83c0b72c83'::uuid, 'classiche', 'a5752b56-fee9-42bd-9bb1-e7ac0b7af9b9'::uuid, 'Classiche'),
       ('d3f3cb68-48af-4896-a681-903d22dd0d86'::uuid, 'elettriche', 'a5752b56-fee9-42bd-9bb1-e7ac0b7af9b9'::uuid, 'Elettriche'),
       ('2c74c3cb-2bc1-4ddd-b2d2-bef6d70f60a6'::uuid, 'batterie-elettriche', '400cdcd7-ccf7-46b6-9a48-9e518d2ca6ca'::uuid, 'Elettriche'),
       ('927bfbc4-2f24-4ffc-ae4e-bbd7a8974145'::uuid, 'batterie-acustiche', '400cdcd7-ccf7-46b6-9a48-9e518d2ca6ca'::uuid, 'Acustiche'),
       ('2632d1b6-7d4a-4224-96c8-d6ca19a2c0ee'::uuid, 'tastiere', NULL, 'Tastiere'),
       ('c0fa0bc2-d935-4159-94d4-11cbe8b73dcb'::uuid, 'pianoforti', '2632d1b6-7d4a-4224-96c8-d6ca19a2c0ee'::uuid, 'Pianoforti'),
       ('0d88bbb9-0f8c-4821-8311-53a6cd3e38be'::uuid, 'tastiere-tastiere', '2632d1b6-7d4a-4224-96c8-d6ca19a2c0ee'::uuid, 'Tastiere'),
       ('65c3c564-e648-447a-a6d7-2212e215bb04'::uuid, 'studio', NULL, 'Studio'),
       ('927bbfff-1650-49e1-9f19-6a71e1199c2a'::uuid, 'interfacce-audio', '65c3c564-e648-447a-a6d7-2212e215bb04'::uuid, 'Interfacce Audio'),
       ('3ff802c3-8ef8-4cb3-bbe0-c219de47943f'::uuid, 'monitor-studio', '65c3c564-e648-447a-a6d7-2212e215bb04'::uuid, 'Monitor Studio'),
       ('3af261f7-32fb-44b2-8853-667f8625957b'::uuid, 'microfoni', '65c3c564-e648-447a-a6d7-2212e215bb04'::uuid, 'Microfoni'),
       ('34cfde52-f006-40f6-b88c-92aeaf4fa1ec'::uuid, 'cuffie', '65c3c564-e648-447a-a6d7-2212e215bb04'::uuid, 'Cuffie'),
       ('3587c8d3-ce82-49e3-abd8-68249b977576'::uuid, 'mixer-digitali', '65c3c564-e648-447a-a6d7-2212e215bb04'::uuid, 'Mixer Digitali'),
       ('9daf88e3-cb39-4c4a-86b1-c0c3ee068728'::uuid, 'convertitori', '65c3c564-e648-447a-a6d7-2212e215bb04'::uuid, 'Convertitori')
ON CONFLICT (id) DO NOTHING;


