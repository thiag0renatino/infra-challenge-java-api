-- Pátios
INSERT INTO patio VALUES (1, 'Pátio Central', 'São Paulo - SP', 'Unidade principal de operações');
INSERT INTO patio VALUES (2, 'Pátio Norte', 'Fortaleza - CE', 'Cobertura ampla e climatizada');
INSERT INTO patio VALUES (3, 'Pátio Sul', 'Porto Alegre - RS', 'Instalação de testes');
INSERT INTO patio VALUES (4, 'Pátio Oeste', 'Cuiabá - MT', 'Alta capacidade de motos');
INSERT INTO patio VALUES (5, 'Pátio Leste', 'Vitória - ES', 'Operação recente');

-- Usuários
INSERT INTO usuario VALUES (1, 'Carlos Oliveira', 'carlos.oliveira@mottu.com', 'HASH1234567890', 'ativo', 'ADMIN', 1);
INSERT INTO usuario VALUES (2, 'Ana Souza', 'ana.souza@mottu.com', 'HASHabcdefghij', 'ativo', 'USER', 2);
INSERT INTO usuario VALUES (3, 'Bruno Lima', 'bruno.lima@mottu.com', 'HASHklmnopqrst', 'ativo', 'USER', 3);
INSERT INTO usuario VALUES (4, 'Marina Rocha', 'marina.rocha@mottu.com', 'HASHuvwxyz9876', 'ativo', 'USER', 4);
INSERT INTO usuario VALUES (5, 'João Pedro', 'joao.pedro@mottu.com', 'HASHsenha2024', 'ativo', 'USER', 5);

-- Motos
INSERT INTO moto VALUES (1, 'ABC1D23', 'Mottu Sport 110i', 'Sem peça', TO_DATE('01/03/2025','DD/MM/YYYY'));
INSERT INTO moto VALUES (2, 'DEF4G56', 'Mottu-e', 'Pronta', TO_DATE('15/04/2025','DD/MM/YYYY'));
INSERT INTO moto VALUES (3, 'GHI7J89', 'Mottu POP', 'Revisão', TO_DATE('20/02/2025','DD/MM/YYYY'));
INSERT INTO moto VALUES (4, 'JKL0M12', 'Mottu Sport 110i', 'Motor', TO_DATE('05/03/2025','DD/MM/YYYY'));
INSERT INTO moto VALUES (5, 'MNO3P45', 'Mottu Sport 110i', 'Pronta', TO_DATE('10/01/2025','DD/MM/YYYY'));
INSERT INTO moto VALUES (6, 'DWE3L09', 'Mottu POP', 'Revisão', TO_DATE('16/01/2025','DD/MM/YYYY'));
INSERT INTO moto VALUES (7, 'KJO3T87', 'Mottu Sport 110i', 'Revisão', TO_DATE('20/02/2025','DD/MM/YYYY'));
INSERT INTO moto VALUES (8, 'YTF4Y91', 'Mottu Sport 110i', 'Motor', TO_DATE('16/04/2025','DD/MM/YYYY'));

-- Posições
INSERT INTO posicao VALUES (1, 2.5, 3.0, TO_DATE('01/05/2025 08:30','DD/MM/YYYY HH24:MI'), 1, 1);
INSERT INTO posicao VALUES (2, 4.1, 5.2, TO_DATE('01/05/2025 09:15','DD/MM/YYYY HH24:MI'), 2, 1);
INSERT INTO posicao VALUES (3, 7.0, 8.0, TO_DATE('01/05/2025 10:00','DD/MM/YYYY HH24:MI'), 3, 2);
INSERT INTO posicao VALUES (4, 1.5, 1.0, TO_DATE('01/05/2025 07:45','DD/MM/YYYY HH24:MI'), 4, 3);
INSERT INTO posicao VALUES (5, 9.0, 6.5, TO_DATE('01/05/2025 11:30','DD/MM/YYYY HH24:MI'), 5, 4);
INSERT INTO posicao VALUES (6, 3.2, 3.7, TO_DATE('02/05/2025 08:15','DD/MM/YYYY HH24:MI'), 1, 1);
INSERT INTO posicao VALUES (7, 4.0, 5.5, TO_DATE('02/05/2025 09:45','DD/MM/YYYY HH24:MI'), 2, 1);
INSERT INTO posicao VALUES (8, 7.5, 8.3, TO_DATE('02/05/2025 10:20','DD/MM/YYYY HH24:MI'), 3, 2);
INSERT INTO posicao VALUES (9, 6.5, 8.0, TO_DATE('03/05/2025 12:40','DD/MM/YYYY HH24:MI'), 6, 4);

-- Marcadores fixos
-- PÁTIO 1
INSERT INTO marcador_fixo VALUES (1, 'ARUCO_001', 0.0, 0.0, 1);
INSERT INTO marcador_fixo VALUES (2, 'ARUCO_002', 10.0, 0.0, 1);
INSERT INTO marcador_fixo VALUES (3, 'ARUCO_003', 5.0, 8.66, 1);
-- PÁTIO 2
INSERT INTO marcador_fixo VALUES (4, 'ARUCO_004', 0.0, 0.0, 2);
INSERT INTO marcador_fixo VALUES (5, 'ARUCO_005', 10.0, 0.0, 2);
INSERT INTO marcador_fixo VALUES (6, 'ARUCO_006', 5.0, 8.66, 2);
-- PÁTIO 3
INSERT INTO marcador_fixo VALUES (7, 'ARUCO_007', 0.0, 0.0, 3);
INSERT INTO marcador_fixo VALUES (8, 'ARUCO_008', 10.0, 0.0, 3);
INSERT INTO marcador_fixo VALUES (9, 'ARUCO_009', 5.0, 8.66, 3);
-- PÁTIO 4
INSERT INTO marcador_fixo VALUES (10, 'ARUCO_010', 0.0, 0.0, 4);
INSERT INTO marcador_fixo VALUES (11, 'ARUCO_011', 10.0, 0.0, 4);
INSERT INTO marcador_fixo VALUES (12, 'ARUCO_012', 5.0, 8.66, 4);
-- PÁTIO 5
INSERT INTO marcador_fixo VALUES (13, 'ARUCO_013', 0.0, 0.0, 5);
INSERT INTO marcador_fixo VALUES (14, 'ARUCO_014', 10.0, 0.0, 5);
INSERT INTO marcador_fixo VALUES (15, 'ARUCO_015', 5.0, 8.66, 5);

-- Marcadores móveis
INSERT INTO marcador_aruco_movel VALUES (1, 'MOVEL_001', TO_DATE('01/03/2025','DD/MM/YYYY'), 1);
INSERT INTO marcador_aruco_movel VALUES (2, 'MOVEL_002', TO_DATE('20/04/2025','DD/MM/YYYY'), 2);
INSERT INTO marcador_aruco_movel VALUES (3, 'MOVEL_003', TO_DATE('18/03/2025','DD/MM/YYYY'), 3);
INSERT INTO marcador_aruco_movel VALUES (4, 'MOVEL_004', TO_DATE('25/04/2025','DD/MM/YYYY'), 4);
INSERT INTO marcador_aruco_movel VALUES (5, 'MOVEL_005', TO_DATE('10/01/2025','DD/MM/YYYY'), 5);

-- Medições
-- POSIÇÕES NO PÁTIO 1 (marcadores 1, 2, 3)
INSERT INTO medicao_posicao VALUES (1, 6.0, 1, 1);
INSERT INTO medicao_posicao VALUES (2, 5.2, 1, 2);
INSERT INTO medicao_posicao VALUES (3, 7.0, 1, 3);

INSERT INTO medicao_posicao VALUES (4, 4.9, 2, 1);
INSERT INTO medicao_posicao VALUES (5, 5.1, 2, 2);
INSERT INTO medicao_posicao VALUES (6, 6.7, 2, 3);

INSERT INTO medicao_posicao VALUES (7, 5.8, 6, 1);
INSERT INTO medicao_posicao VALUES (8, 5.0, 6, 2);
INSERT INTO medicao_posicao VALUES (9, 6.9, 6, 3);

INSERT INTO medicao_posicao VALUES (10, 6.1, 7, 1);
INSERT INTO medicao_posicao VALUES (11, 5.3, 7, 2);
INSERT INTO medicao_posicao VALUES (12, 7.1, 7, 3);

-- POSIÇÕES NO PÁTIO 2 (marcadores 4, 5, 6)
INSERT INTO medicao_posicao VALUES (13, 7.0, 3, 4);
INSERT INTO medicao_posicao VALUES (14, 6.2, 3, 5);
INSERT INTO medicao_posicao VALUES (15, 8.0, 3, 6);

INSERT INTO medicao_posicao VALUES (16, 7.1, 8, 4);
INSERT INTO medicao_posicao VALUES (17, 6.5, 8, 5);
INSERT INTO medicao_posicao VALUES (18, 7.8, 8, 6);

-- POSIÇÃO NO PÁTIO 3 (marcadores 7, 8, 9)
INSERT INTO medicao_posicao VALUES (19, 4.8, 4, 7);
INSERT INTO medicao_posicao VALUES (20, 5.0, 4, 8);
INSERT INTO medicao_posicao VALUES (21, 6.2, 4, 9);

-- POSIÇÃO NO PÁTIO 4 (marcadores 10, 11, 12)
INSERT INTO medicao_posicao VALUES (22, 5.9, 5, 10);
INSERT INTO medicao_posicao VALUES (23, 5.1, 5, 11);
INSERT INTO medicao_posicao VALUES (24, 6.5, 5, 12);
