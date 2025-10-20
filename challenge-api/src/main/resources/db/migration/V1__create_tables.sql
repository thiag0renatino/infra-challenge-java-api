CREATE TABLE patio (
  id_patio INTEGER PRIMARY KEY,
  nome VARCHAR2(100),
  localizacao VARCHAR2(100),
  descricao VARCHAR2(255)
);

CREATE TABLE moto (
  id_moto INTEGER PRIMARY KEY,
  placa VARCHAR2(7) UNIQUE,
  modelo VARCHAR2(50),
  status VARCHAR2(65),
  data_cadastro DATE
);

CREATE TABLE marcador_fixo (
  id_marcador_aruco_fixo INTEGER PRIMARY KEY,
  codigo_aruco VARCHAR2(50),
  x_pos FLOAT(10),
  y_pos FLOAT(10),
  patio_id_patio INTEGER REFERENCES patio(id_patio)
);

CREATE TABLE marcador_aruco_movel (
  id_marcador_movel INTEGER PRIMARY KEY,
  codigo_aruco VARCHAR2(50),
  data_instalacao DATE,
  moto_id_moto INTEGER REFERENCES moto(id_moto)
);

CREATE TABLE posicao (
  id_posicao INTEGER PRIMARY KEY,
  x_pos FLOAT(10),
  y_pos FLOAT(10),
  data_hora DATE,
  moto_id_moto INTEGER REFERENCES moto(id_moto),
  patio_id_patio INTEGER REFERENCES patio(id_patio)
);

CREATE TABLE medicao_posicao (
  id_medicao INTEGER PRIMARY KEY,
  distancia_m FLOAT(10),
  posicao_id_posicao INTEGER REFERENCES posicao(id_posicao),
  marcador_fixo_id_marcador_aruco_fixo INTEGER REFERENCES marcador_fixo(id_marcador_aruco_fixo)
);

CREATE TABLE usuario (
  id_usuario INTEGER PRIMARY KEY,
  nome VARCHAR2(100) NOT NULL,
  email VARCHAR2(100) UNIQUE NOT NULL,
  senha VARCHAR2(255) NOT NULL,
  status VARCHAR2(20) DEFAULT 'ativo',
  tipo VARCHAR2(20) CHECK (tipo IN ('USER', 'ADMIN')),
  patio_id_patio INTEGER NOT NULL,
  CONSTRAINT usuario_patio_FK FOREIGN KEY (patio_id_patio) REFERENCES patio(id_patio)
);