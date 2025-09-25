-- liquibase formatted sql

-- changeset riccardo:1758809323842-6
INSERT INTO public.prodotto_categoria (categoria_id,prodotto_id) VALUES
     ('3587c8d3-ce82-49e3-abd8-68249b977576'::uuid,'a1b2c3d4-e5f6-4a7b-8c9d-1e2f3a4b5c6d'::uuid),
     ('927bbfff-1650-49e1-9f19-6a71e1199c2a'::uuid,'364098ca-bced-4466-b9d8-f76dc1e25ffc'::uuid),
     ('927bbfff-1650-49e1-9f19-6a71e1199c2a'::uuid,'f3d1a2b3-e4f5-4a6b-8c9d-0e1f2a3b4c5d'::uuid),
     ('d3f3cb68-48af-4896-a681-903d22dd0d86'::uuid,'5721081f-8ca4-43f5-9036-c787d7fc024c'::uuid),
     ('d3f3cb68-48af-4896-a681-903d22dd0d86'::uuid,'6b8a891d-7bb7-47b5-aede-2e7e70fae010'::uuid),
     ('e23e74ee-abf3-4ce0-b955-c81dc9b88f52'::uuid,'938ad12a-0f5f-463e-b71c-c2e27dfaead8'::uuid),
     ('e23e74ee-abf3-4ce0-b955-c81dc9b88f52'::uuid,'a91410e3-64fa-4271-b4b8-4fcb98757a3d'::uuid),
     ('a5a56e70-c609-42f2-90ce-5d83c0b72c83'::uuid,'c29b5724-5991-45f0-bffb-69ff70db7d99'::uuid),
     ('a5a56e70-c609-42f2-90ce-5d83c0b72c83'::uuid,'34c09f57-fdb7-45e2-bfb4-27638165f7a0'::uuid),
     ('927bfbc4-2f24-4ffc-ae4e-bbd7a8974145'::uuid,'05b56dd7-a85c-435f-8926-390f0d9f95ea'::uuid),
     ('927bfbc4-2f24-4ffc-ae4e-bbd7a8974145'::uuid,'8d5ecf64-ddc3-4bcf-8e1a-ccf0ada22d93'::uuid),
     ('2c74c3cb-2bc1-4ddd-b2d2-bef6d70f60a6'::uuid,'ec1673d2-3d31-4591-9bef-78ed6c265dc4'::uuid),
     ('2c74c3cb-2bc1-4ddd-b2d2-bef6d70f60a6'::uuid,'814e5967-5a70-467c-960e-f6ec708a3be3'::uuid),
     ('0d88bbb9-0f8c-4821-8311-53a6cd3e38be'::uuid,'10d8ee06-15c1-4f73-aa28-29551d7cbed9'::uuid),
     ('0d88bbb9-0f8c-4821-8311-53a6cd3e38be'::uuid,'c39af9fe-4539-408c-8c41-be2d624301c2'::uuid),
     ('c0fa0bc2-d935-4159-94d4-11cbe8b73dcb'::uuid,'8fe938b4-a829-45ed-9712-27458d5b0321'::uuid),
     ('c0fa0bc2-d935-4159-94d4-11cbe8b73dcb'::uuid,'e86d8627-1abb-48b3-834d-0c47b8535a51'::uuid),
     ('3ff802c3-8ef8-4cb3-bbe0-c219de47943f'::uuid,'918321e7-30ce-4274-bb68-c557c7c44f56'::uuid),
     ('3ff802c3-8ef8-4cb3-bbe0-c219de47943f'::uuid,'d2077879-7ec3-4783-a793-350ea44a3845'::uuid),
     ('3af261f7-32fb-44b2-8853-667f8625957b'::uuid,'12f36cf6-d5ea-40aa-90bf-8d69d02eaa7f'::uuid),
     ('3af261f7-32fb-44b2-8853-667f8625957b'::uuid,'932b611f-f185-4287-86be-717f16510417'::uuid),
     ('34cfde52-f006-40f6-b88c-92aeaf4fa1ec'::uuid,'31b2b04a-b5c0-45b5-8546-c95049b54b26'::uuid),
     ('34cfde52-f006-40f6-b88c-92aeaf4fa1ec'::uuid,'3425c753-8978-476e-8ca7-71641565dd88'::uuid),
     ('34cfde52-f006-40f6-b88c-92aeaf4fa1ec'::uuid,'43e2cff0-4200-4702-b037-8faadad8b391'::uuid),
     ('9daf88e3-cb39-4c4a-86b1-c0c3ee068728'::uuid,'497fe185-21d3-4a05-8bb6-c9e99e87b922'::uuid),
     ('9daf88e3-cb39-4c4a-86b1-c0c3ee068728'::uuid,'b5afd89b-049a-4b90-b34f-9cc38ef6a146'::uuid),
     ('9daf88e3-cb39-4c4a-86b1-c0c3ee068728'::uuid,'ab8c39df-18b5-4e5f-aeaa-c3fd881a7656'::uuid),
     ('3587c8d3-ce82-49e3-abd8-68249b977576'::uuid,'e8b9b4a0-c8f4-4e9a-9b1a-2b3c4d5e6f7a'::uuid)
ON CONFLICT (categoria_id, prodotto_id) DO NOTHING;


