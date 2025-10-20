-------------------------
-- PATIO (1..5)
-------------------------
INSERT INTO dbo.patio (id_patio, nome, localizacao, descricao) VALUES
(1, N'Pátio Central', N'São Paulo - SP',  N'Unidade principal de operações'),
(2, N'Pátio Norte',   N'Fortaleza - CE',  N'Cobertura ampla e climatizada'),
(3, N'Pátio Sul',     N'Porto Alegre - RS', N'Instalação de testes'),
(4, N'Pátio Oeste',   N'Cuiabá - MT',     N'Alta capacidade de motos'),
(5, N'Pátio Leste',   N'Vitória - ES',    N'Operação recente');

-------------------------
-- USUARIO (1..5)
-------------------------
INSERT INTO dbo.usuario (id_usuario, nome, email, senha, status, tipo, patio_id_patio) VALUES
(1, N'Carlos Oliveira', N'carlos.oliveira@mottu.com', N'HASH1234567890', N'ativo', N'ADMIN', 1),
(2, N'Ana Souza',       N'ana.souza@mottu.com',       N'HASHabcdefghij', N'ativo', N'USER',  2),
(3, N'Bruno Lima',      N'bruno.lima@mottu.com',      N'HASHklmnopqrst', N'ativo', N'USER',  3),
(4, N'Marina Rocha',    N'marina.rocha@mottu.com',    N'HASHuvwxyz9876', N'ativo', N'USER',  4),
(5, N'João Pedro',      N'joao.pedro@mottu.com',      N'HASHsenha2024',  N'ativo', N'USER',  5);

-------------------------
-- MOTO (1..8)
-------------------------
INSERT INTO dbo.moto (id_moto, placa, modelo, status, data_cadastro) VALUES
(1, N'ABC1D23', N'Mottu Sport 110i', N'Sem peça', '2025-03-01'),
(2, N'DEF4G56', N'Mottu-e',          N'Pronta',   '2025-04-15'),
(3, N'GHI7J89', N'Mottu POP',        N'Revisão',  '2025-02-20'),
(4, N'JKL0M12', N'Mottu Sport 110i', N'Motor',    '2025-03-05'),
(5, N'MNO3P45', N'Mottu Sport 110i', N'Pronta',   '2025-01-10'),
(6, N'DWE3L09', N'Mottu POP',        N'Revisão',  '2025-01-16'),
(7, N'KJO3T87', N'Mottu Sport 110i', N'Revisão',  '2025-02-20'),
(8, N'YTF4Y91', N'Mottu Sport 110i', N'Motor',    '2025-04-16');

-------------------------
-- POSICAO (1..9)
-------------------------
INSERT INTO dbo.posicao (id_posicao, x_pos, y_pos, data_hora, moto_id_moto, patio_id_patio) VALUES
(1, 2.5, 3.0, '2025-05-01T08:30:00', 1, 1),
(2, 4.1, 5.2, '2025-05-01T09:15:00', 2, 1),
(3, 7.0, 8.0, '2025-05-01T10:00:00', 3, 2),
(4, 1.5, 1.0, '2025-05-01T07:45:00', 4, 3),
(5, 9.0, 6.5, '2025-05-01T11:30:00', 5, 4),
(6, 3.2, 3.7, '2025-05-02T08:15:00', 1, 1),
(7, 4.0, 5.5, '2025-05-02T09:45:00', 2, 1),
(8, 7.5, 8.3, '2025-05-02T10:20:00', 3, 2),
(9, 6.5, 8.0, '2025-05-03T12:40:00', 6, 4);

-------------------------
-- MARCADOR_FIXO (1..15)
-------------------------
-- PÁTIO 1
INSERT INTO dbo.marcador_fixo (id_marcador_aruco_fixo, codigo_aruco, x_pos, y_pos, patio_id_patio) VALUES
(1,  N'ARUCO_001', 0.0,  0.0,  1),
(2,  N'ARUCO_002', 10.0, 0.0,  1),
(3,  N'ARUCO_003', 5.0,  8.66, 1);
-- PÁTIO 2
INSERT INTO dbo.marcador_fixo (id_marcador_aruco_fixo, codigo_aruco, x_pos, y_pos, patio_id_patio) VALUES
(4,  N'ARUCO_004', 0.0,  0.0,  2),
(5,  N'ARUCO_005', 10.0, 0.0,  2),
(6,  N'ARUCO_006', 5.0,  8.66, 2);
-- PÁTIO 3
INSERT INTO dbo.marcador_fixo (id_marcador_aruco_fixo, codigo_aruco, x_pos, y_pos, patio_id_patio) VALUES
(7,  N'ARUCO_007', 0.0,  0.0,  3),
(8,  N'ARUCO_008', 10.0, 0.0,  3),
(9,  N'ARUCO_009', 5.0,  8.66, 3);
-- PÁTIO 4
INSERT INTO dbo.marcador_fixo (id_marcador_aruco_fixo, codigo_aruco, x_pos, y_pos, patio_id_patio) VALUES
(10, N'ARUCO_010', 0.0,  0.0,  4),
(11, N'ARUCO_011', 10.0, 0.0,  4),
(12, N'ARUCO_012', 5.0,  8.66, 4);
-- PÁTIO 5
INSERT INTO dbo.marcador_fixo (id_marcador_aruco_fixo, codigo_aruco, x_pos, y_pos, patio_id_patio) VALUES
(13, N'ARUCO_013', 0.0,  0.0,  5),
(14, N'ARUCO_014', 10.0, 0.0,  5),
(15, N'ARUCO_015', 5.0,  8.66, 5);

-------------------------
-- MARCADOR_ARUCO_MOVEL (1..5)
-------------------------
INSERT INTO dbo.marcador_aruco_movel (id_marcador_movel, codigo_aruco, data_instalacao, moto_id_moto) VALUES
(1, N'MOVEL_001', '2025-03-01', 1),
(2, N'MOVEL_002', '2025-04-20', 2),
(3, N'MOVEL_003', '2025-03-18', 3),
(4, N'MOVEL_004', '2025-04-25', 4),
(5, N'MOVEL_005', '2025-01-10', 5);

-------------------------
-- MEDICAO_POSICAO (1..24)
-------------------------
-- PÁTIO 1 (marcadores 1,2,3)
INSERT INTO dbo.medicao_posicao (id_medicao, distancia_m, posicao_id_posicao, marcador_fixo_id_marcador_aruco_fixo) VALUES
(1,  6.0, 1, 1),
(2,  5.2, 1, 2),
(3,  7.0, 1, 3),
(4,  4.9, 2, 1),
(5,  5.1, 2, 2),
(6,  6.7, 2, 3),
(7,  5.8, 6, 1),
(8,  5.0, 6, 2),
(9,  6.9, 6, 3),
(10, 6.1, 7, 1),
(11, 5.3, 7, 2),
(12, 7.1, 7, 3);

-- PÁTIO 2 (marcadores 4,5,6)
INSERT INTO dbo.medicao_posicao (id_medicao, distancia_m, posicao_id_posicao, marcador_fixo_id_marcador_aruco_fixo) VALUES
(13, 7.0, 3, 4),
(14, 6.2, 3, 5),
(15, 8.0, 3, 6),
(16, 7.1, 8, 4),
(17, 6.5, 8, 5),
(18, 7.8, 8, 6);

-- PÁTIO 3 (marcadores 7,8,9)
INSERT INTO dbo.medicao_posicao (id_medicao, distancia_m, posicao_id_posicao, marcador_fixo_id_marcador_aruco_fixo) VALUES
(19, 4.8, 4, 7),
(20, 5.0, 4, 8),
(21, 6.2, 4, 9);

-- PÁTIO 4 (marcadores 10,11,12)
INSERT INTO dbo.medicao_posicao (id_medicao, distancia_m, posicao_id_posicao, marcador_fixo_id_marcador_aruco_fixo) VALUES
(22, 5.9, 5, 10),
(23, 5.1, 5, 11),
(24, 6.5, 5, 12);