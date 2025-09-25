-- liquibase formatted sql

-- changeset riccardo:1758809323842-3

INSERT INTO public.prodotto (id, costo_giornaliero, descrizione, is_rentable, marca, nome, path_immagine, prezzo) VALUES
      ('05b56dd7-a85c-435f-8926-390f0d9f95ea'::uuid, 80.0, 'Batteria - set completo Serie Energy Studio Version...', false, 'Gretsch', 'Drums Energy Studio Black', '/images/e466c013-004a-453f-b07c-5ed647d405e9.png', 888.0),
      ('8d5ecf64-ddc3-4bcf-8e1a-ccf0ada22d93'::uuid, 85.0, 'Batteria - set completo Serie Roadshow...', false, 'Pearl', 'Roadshow 22" Plus Matte Red', '/images/7e120a99-4fc1-4370-8609-58b20046d777.png', 859.0),
      ('918321e7-30ce-4274-bb68-c557c7c44f56'::uuid, 60.0, 'Monitor attivo nearfield Progettato senza apertura posteriore...', false, 'Focal', 'Shape 65', '/images/c10c386f-49b1-46eb-b737-b508206fb4e7.png', 629.0),
      ('e8b9b4a0-c8f4-4e9a-9b1a-2b3c4d5e6f7a'::uuid, 10.0, 'cambio di nome per test', true, 'Midas', 'MR 18', '/images/11a1bd1c-57e7-4acd-9beb-147e467ab404.png', 800.0),
      ('a1b2c3d4-e5f6-4a7b-8c9d-1e2f3a4b5c6d'::uuid, 300.0, 'Mixer digitale a 48 in e 36 aux', true, 'Hallen & Heat', 'SQ7', '/images/053991c8-401d-4315-a275-750e4ff8c8af.png', 5000.0),
      ('364098ca-bced-4466-b9d8-f76dc1e25ffc'::uuid, 40.0, 'Interfaccia audio USB 2.0 Convertitore AD/DA a 24 bit/192 kHz...', false, 'RME', 'Fireface UCX II', '/images/68cea51c-15a2-4db4-8163-b880348c9ba0.png', 1299.0),
      ('f3d1a2b3-e4f5-4a6b-8c9d-0e1f2a3b4c5d'::uuid, 10.0, 'Scheda audio focusrite 2i2', true, 'Focusrite', 'Scarlett 2i2', '/images/d3e9fcc6-a091-44cc-9303-b99b25d796b8.png', 140.0),
      ('5721081f-8ca4-43f5-9036-c787d7fc024c'::uuid, 15.0, 'Chitarra elettrica Corpo: pioppo Manico avvitato: acero...', false, 'Harley Benton', 'ST-20HH Active SBK', '/images/0dfea372-3669-4410-b630-37d1b9e23e0f.png', 149.0),
      ('6b8a891d-7bb7-47b5-aede-2e7e70fae010'::uuid, 100.0, 'Chitarra elettrica Collezione custom Inspired by Gibson 2025...', false, 'Epiphone', '1960 Les Paul Special DC TVY', '/images/e56fb200-ad46-48dd-93ad-45ce927d2bcc.png', 1050.0),
      ('938ad12a-0f5f-463e-b71c-c2e27dfaead8'::uuid, 8.0, 'Chitarra acustica Forma del corpo: Dreadnought con cutaway...', false, 'Harley Benton', 'D-120CE NT', '/images/0357e718-d394-4662-8f5b-5f5569ac28f1.png', 89.0),
      ('a91410e3-64fa-4271-b4b8-4fcb98757a3d'::uuid, 100.0, 'Chitarra acustica con pick-up Inspired by Gibson Custom...', false, 'Epiphone', 'Hummingbird Deluxe EC AB IBC', '/images/eafcafb4-816e-4516-9291-854115721e32.png', 1290.0),
      ('c29b5724-5991-45f0-bffb-69ff70db7d99'::uuid, 20.0, 'Chitarra classica con pick-up Forma del corpo: con cutaway...', false, 'Yamaha', 'NTX1NT NAT', '/images/2d384ca7-b3bd-4d01-af21-6c9f8984352e.png', 409.0),
      ('34c09f57-fdb7-45e2-bfb4-27638165f7a0'::uuid, 200.0, 'Chitarra classica con pick-up Forma del corpo: Grand Auditorium...', false, 'Taylor', '814ce-N', '/images/d2ddcb36-7a64-4f03-aff7-ab9076a2c7b3.png', 3599.0),
      ('ec1673d2-3d31-4591-9bef-78ed6c265dc4'::uuid, 87.0, 'Roland SPD-SX PRO Sampling Pad Pad di campionamento...', false, 'Roland', 'SPD-SX PRO Sampling Pad Bundle', '/images/ff218c18-bdc5-401f-ac5a-84c3abdcfd15.png', 879.0),
      ('814e5967-5a70-467c-960e-f6ec708a3be3'::uuid, 70.0, 'Set di batteria elettronica Con pelli mesh per un rebound naturale...', false, 'Alesis', 'Nitro Pro XL Drum Kit', '/images/6c9c83d1-3133-4800-97b5-2394e70aaa52.png', 729.0),
      ('10d8ee06-15c1-4f73-aa28-29551d7cbed9'::uuid, 200.0, 'Workstation digitale 76 tasti (FSX) con sensibilità al tocco...', false, 'Yamaha', 'Genos 2', '/images/ab22c934-fc8b-45ee-9fc9-7760df506ca2.png', 3990.0),
      ('c39af9fe-4539-408c-8c41-be2d624301c2'::uuid, 300.0, 'Performance Stage Piano 88 tasti pesati con aftertouch...', false, 'Clavia', 'Nord Stage 4 88', '/images/f86b9983-eaf6-42ad-8260-d5168e6e43cd.png', 4399.0),
      ('8fe938b4-a829-45ed-9712-27458d5b0321'::uuid, 400.0, 'Pianoforte a coda Il nuovo pianoforte a coda GC 1...', false, 'Yamaha', 'GC 1 M PE Grand Piano', '/images/ca881bb7-366f-4470-85e5-67150503479d.png', 19390.0),
      ('e86d8627-1abb-48b3-834d-0c47b8535a51'::uuid, 3000.0, 'Pianoforte a coda Strumento usato - meticolosamente revisionato...', false, 'Steinway & Sons', 'B-211', '/images/9028ad4b-2a12-4f4c-bb2c-057ee5bf8457.png', 57800.0),
      ('d2077879-7ec3-4783-a793-350ea44a3845'::uuid, 10.0, 'Monitor Nearfield attivo a 2 vie Altoparlanti: Woofer da 6,5"...', false, 'Yamaha', 'HS 7', '/images/7a1be234-5cb8-4561-ab0f-ba6c96b94b02.png', 229.0),
      ('12f36cf6-d5ea-40aa-90bf-8d69d02eaa7f'::uuid, 30.0, 'Microfono dinamico Figura polare: cardioide...', false, 'Shure', 'SM 7 B', '/images/b57d04e7-c148-4e65-91c2-8e05c08d1fd4.png', 389.0),
      ('932b611f-f185-4287-86be-717f16510417'::uuid, 100.0, 'Microfono a diaframma largo Design della capsula basato sul leggendario U 87...', false, 'Neumann', 'TLM 103', '/images/ad8eeab2-744d-4808-8c23-a0efc470b62a.png', 1033.0),
      ('31b2b04a-b5c0-45b5-8546-c95049b54b26'::uuid, 15.0, 'Cuffie da studio Chiuse Circumaurale Dinamico...', false, 'beyerdynamic', 'DT-770 Pro 80 Ohm', '/images/70d36d11-33fe-4b19-ad64-87fb898db375.png', 149.0),
      ('3425c753-8978-476e-8ca7-71641565dd88'::uuid, 20.0, 'Cuffie Dinamiche Aperte Circumaurali...', false, 'Sennheiser', 'HD 600', '/images/1820f603-ffd6-4d4e-9f25-7b10a65e5265.png', 319.0),
      ('43e2cff0-4200-4702-b037-8faadad8b391'::uuid, 15.0, 'Cuffie da studio Driver da 45 mm con sistema di magneti al neodimio...', false, 'Audio-Technica', 'ATH-M50X', '/images/01f04da6-199b-475b-9426-94c54e028956.png', 149.0),
      ('497fe185-21d3-4a05-8bb6-c9e99e87b922'::uuid, 70.0, 'Convertitore DA e amplificatore per cuffie di alta gamma...', false, 'RME', 'ADI-2 DAC FS', '/images/2d041c97-25e9-4c96-a6dc-31663b967bcf.png', 839.0),
      ('b5afd89b-049a-4b90-b34f-9cc38ef6a146'::uuid, 300.0, 'Interfaccia audio DigiLink a 34 canali per Pro Tools Ultimate...', false, 'Avid', 'Pro Tools MTRX Studio', '/images/64953191-c19e-477c-a833-2c6581fea720.png', 5333.0),
      ('ab8c39df-18b5-4e5f-aeaa-c3fd881a7656'::uuid, 300.0, 'Modular audio recording interface with Thunderbolt connection...', false, 'Apogee', 'Symphony I/O Mk2 16x16 SE TB', '/images/f1349cc0-8736-41b3-9e60-83993371e917.png', 6222.0)
ON CONFLICT (id) DO NOTHING;
