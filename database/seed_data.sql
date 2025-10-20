-- PÁTIO
INSERT INTO dbo.patio (id_patio, nome, localizacao, descricao) VALUES
(1, 'Pátio Central', 'São Paulo - SP', 'Unidade principal de operações'),
(2, 'Pátio Norte',   'Fortaleza - CE', 'Cobertura ampla e climatizada'),
(3, 'Pátio Sul',     'Porto Alegre - RS', 'Instalação de testes'),
(4, 'Pátio Oeste',   'Cuiabá - MT', 'Alta capacidade de motos'),
(5, 'Pátio Leste',   'Vitória - ES', 'Operação recente');

-- USUÁRIO (hashes placeholder)
INSERT INTO dbo.usuario (id_usuario, nome, email, senha, status, tipo, patio_id_patio) VALUES
(1, 'Carlos Oliveira', 'carlos.oliveira@mottu.com', 'HASH1234567890', 'ativo', 'ADMIN', 1),
(2, 'Ana Souza',       'ana.souza@mottu.com',       'HASHabcdefghij', 'ativo', 'USER',  2),
(3, 'Bruno Lima',      'bruno.lima@mottu.com',      'HASHklmnopqrst', 'ativo', 'USER',  3),
(4, 'Marina Rocha',    'marina.rocha@mottu.com',    'HASHuvwxyz9876', 'ativo', 'USER',  4),
(5, 'João Pedro',      'joao.pedro@mottu.com',      'HASHsenha2024',  'ativo', 'USER',  5);

-- MOTO
INSERT INTO dbo.moto (id_moto, placa, modelo, status, data_cadastro) VALUES
(1, 'ABC1D23', 'Mottu Sport 110i', 'Sem peça', '2025-03-01'),
(2, 'DEF4G56', 'Mottu-e',          'Pronta',    '2025-04-15'),
(3, 'GHI7J89', 'Mottu POP',        'Revisão',   '2025-02-20'),
(4, 'JKL0M12', 'Mottu Sport 110i', 'Motor',     '2025-03-05'),
(5, 'MNO3P45', 'Mottu Sport 110i', 'Pronta',    '2025-01-10'),
(6, 'DWE3L09', 'Mottu POP',        'Revisão',   '2025-01-16'),
(7, 'KJO3T87', 'Mottu Sport 110i', 'Revisão',   '2025-02-20'),
(8, 'YTF4Y91', 'Mottu Sport 110i', 'Motor',     '2025-04-16');

-- POSIÇÃO (usar DATETIME2 ISO 8601)
INSERT INTO dbo.posicao (id_posicao, x_pos, y_pos, data_hora, moto_id_moto, patio_id_patio) VALUES
(1, 2.5, 3.0,  '2025-05-01T08:30:00', 1, 1),
(2, 4.1, 5.2,  '2025-05-01T09:15:00', 2, 1),
(3, 7.0, 8.0,  '2025-05-01T10:00:00', 3, 2),
(4, 1.5, 1.0,  '2025-05-01T07:45:00', 4, 3),
(5, 9.0, 6.5,  '2025-05-01T11:30:00', 5, 4),
(6, 3.2, 3.7,  '2025-05-02T08:15:00', 1, 1),
(7, 4.0, 5.5,  '2025-05-02T09:45:00', 2, 1),
(8, 7.5, 8.3,  '2025-05-02T10:20:00', 3, 2),
(9, 6.5, 8.0,  '2025-05-03T12:40:00', 6, 4);

-- MARCADOR_FIXO (pátios 1..5)
INSERT INTO dbo.marcador_fixo (id_marcador_aruco_fixo, codigo_aruco, x_pos, y_pos, patio_id_patio) VALUES
(1,  'ARUCO_001', 0.0,  0.0,  1),
(2,  'ARUCO_002', 10.0, 0.0,  1),
(3,  'ARUCO_003', 5.0,  8.66, 1),

(4,  'ARUCO_004', 0.0,  0.0,  2),
(5,  'ARUCO_005', 10.0, 0.0,  2),
(6,  'ARUCO_006', 5.0,  8.66, 2),

(7,  'ARUCO_007', 0.0,  0.0,  3),
(8,  'ARUCO_008', 10.0, 0.0,  3),
(9,  'ARUCO_009', 5.0,  8.66, 3),

(10, 'ARUCO_010', 0.0,  0.0,  4),
(11, 'ARUCO_011', 10.0, 0.0,  4),
(12, 'ARUCO_012', 5.0,  8.66, 4),

(13, 'ARUCO_013', 0.0,  0.0,  5),
(14, 'ARUCO_014', 10.0, 0.0,  5),
(15, 'ARUCO_015', 5.0,  8.66, 5);

-- MARCADOR_ARUCO_MOVEL
INSERT INTO dbo.marcador_aruco_movel (id_marcador_movel, codigo_aruco, data_instalacao, moto_id_moto) VALUES
(1, 'MOVEL_001', '2025-03-01', 1),
(2, 'MOVEL_002', '2025-04-20', 2),
(3, 'MOVEL_003', '2025-03-18', 3),
(4, 'MOVEL_004', '2025-04-25', 4),
(5, 'MOVEL_005', '2025-01-10', 5);

-- MEDICAO_POSICAO (pátio 1)
INSERT INTO dbo.medicao_posicao (id_medicao, distancia_m, posicao_id_posicao, marcador_fixo_id_marcador_aruco_fixo) VALUES
(1,  6.0, 1, 1), (2,  5.2, 1, 2), (3,  7.0, 1, 3),
(4,  4.9, 2, 1), (5,  5.1, 2, 2), (6,  6.7, 2, 3),
(7,  5.8, 6, 1), (8,  5.0, 6, 2), (9,  6.9, 6, 3),
(10, 6.1, 7, 1), (11, 5.3, 7, 2), (12, 7.1, 7, 3),

-- pátio 2
(13, 7.0, 3, 4), (14, 6.2, 3, 5), (15, 8.0, 3, 6),
(16, 7.1, 8, 4), (17, 6.5, 8, 5), (18, 7.8, 8, 6),

-- pátio 3
(19, 4.8, 4, 7), (20, 5.0, 4, 8), (21, 6.2, 4, 9),

-- pátio 4
(22, 5.9, 5, 10), (23, 5.1, 5, 11), (24, 6.5, 5, 12);

