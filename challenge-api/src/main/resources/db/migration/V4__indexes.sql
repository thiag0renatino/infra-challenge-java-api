-- Índices para acelerar buscas e junções por chaves estrangeiras

-- POSICAO: FKs
CREATE INDEX IX_POSICAO_MOTO   ON posicao(moto_id_moto);
CREATE INDEX IX_POSICAO_PATIO  ON posicao(patio_id_patio);

-- MARCADOR_FIXO: FK
CREATE INDEX IX_MFIXO_PATIO    ON marcador_fixo(patio_id_patio);

-- MEDICAO_POSICAO: FKs
CREATE INDEX IX_MEDPOS_POSICAO ON medicao_posicao(posicao_id_posicao);
CREATE INDEX IX_MEDPOS_MFIXO   ON medicao_posicao(marcador_fixo_id_marcador_aruco_fixo);

-- USUARIO: FK
CREATE INDEX IX_USUARIO_PATIO  ON usuario(patio_id_patio);

