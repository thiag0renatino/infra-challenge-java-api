
-- SEQUENCES (mesmos starts do Oracle)
CREATE SEQUENCE dbo.SEQ_PATIO_CH         START WITH 6  INCREMENT BY 1;
CREATE SEQUENCE dbo.SEQ_USUARIO_CH       START WITH 6  INCREMENT BY 1;
CREATE SEQUENCE dbo.SEQ_MOTO_CH          START WITH 9  INCREMENT BY 1;
CREATE SEQUENCE dbo.SEQ_POS_CH           START WITH 10 INCREMENT BY 1;
CREATE SEQUENCE dbo.SEQ_MARCADOR_F_CH    START WITH 16 INCREMENT BY 1;
CREATE SEQUENCE dbo.SEQ_MARCADOR_M_CH    START WITH 6  INCREMENT BY 1;
CREATE SEQUENCE dbo.SEQ_MEDICAO_CH       START WITH 26 INCREMENT BY 1;

---------------------------------------------------------------
-- PATIO
---------------------------------------------------------------
CREATE TABLE dbo.patio (
  id_patio     INT            NOT NULL PRIMARY KEY,
  nome         NVARCHAR(100)  NULL,
  localizacao  NVARCHAR(100)  NULL,
  descricao    NVARCHAR(255)  NULL
);

ALTER TABLE dbo.patio
  ADD CONSTRAINT DF_PATIO_ID
  DEFAULT NEXT VALUE FOR dbo.SEQ_PATIO_CH FOR id_patio;

---------------------------------------------------------------
-- USUARIO
---------------------------------------------------------------
CREATE TABLE dbo.usuario (
  id_usuario     INT            NOT NULL PRIMARY KEY,
  nome           NVARCHAR(100)  NOT NULL,
  email          NVARCHAR(100)  NOT NULL UNIQUE,
  senha          NVARCHAR(255)  NOT NULL,
  status         NVARCHAR(20)   NULL CONSTRAINT DF_USUARIO_STATUS DEFAULT N'ativo',
  tipo           NVARCHAR(20)   NULL,
  patio_id_patio INT            NOT NULL,
  CONSTRAINT CK_USUARIO_TIPO CHECK (tipo IN (N'USER', N'ADMIN')),
  CONSTRAINT usuario_patio_FK FOREIGN KEY (patio_id_patio) REFERENCES dbo.patio(id_patio)
);

ALTER TABLE dbo.usuario
  ADD CONSTRAINT DF_USUARIO_ID
  DEFAULT NEXT VALUE FOR dbo.SEQ_USUARIO_CH FOR id_usuario;

---------------------------------------------------------------
-- MOTO
---------------------------------------------------------------
CREATE TABLE dbo.moto (
  id_moto        INT            NOT NULL PRIMARY KEY,
  placa          NVARCHAR(7)    NULL UNIQUE,
  modelo         NVARCHAR(50)   NULL,
  status         NVARCHAR(65)   NULL,
  data_cadastro  DATE           NULL
);

ALTER TABLE dbo.moto
  ADD CONSTRAINT DF_MOTO_ID
  DEFAULT NEXT VALUE FOR dbo.SEQ_MOTO_CH FOR id_moto;

---------------------------------------------------------------
-- POSICAO
---------------------------------------------------------------
CREATE TABLE dbo.posicao (
  id_posicao     INT           NOT NULL PRIMARY KEY,
  x_pos          FLOAT         NULL,
  y_pos          FLOAT         NULL,
  data_hora      DATETIME2(0)  NULL,
  moto_id_moto   INT           NULL,
  patio_id_patio INT           NULL,
  CONSTRAINT posicao_moto_FK  FOREIGN KEY (moto_id_moto)   REFERENCES dbo.moto(id_moto),
  CONSTRAINT posicao_patio_FK FOREIGN KEY (patio_id_patio) REFERENCES dbo.patio(id_patio)
);

ALTER TABLE dbo.posicao
  ADD CONSTRAINT DF_POSICAO_ID
  DEFAULT NEXT VALUE FOR dbo.SEQ_POS_CH FOR id_posicao;

---------------------------------------------------------------
-- MARCADOR_FIXO
---------------------------------------------------------------
CREATE TABLE dbo.marcador_fixo (
  id_marcador_aruco_fixo INT           NOT NULL PRIMARY KEY,
  codigo_aruco           NVARCHAR(50)  NULL,
  x_pos                  FLOAT         NULL,
  y_pos                  FLOAT         NULL,
  patio_id_patio         INT           NULL,
  CONSTRAINT marcador_fixo_patio_FK FOREIGN KEY (patio_id_patio) REFERENCES dbo.patio(id_patio)
);

ALTER TABLE dbo.marcador_fixo
  ADD CONSTRAINT DF_MARCADOR_FIXO_ID
  DEFAULT NEXT VALUE FOR dbo.SEQ_MARCADOR_F_CH FOR id_marcador_aruco_fixo;

---------------------------------------------------------------
-- MARCADOR_ARUCO_MOVEL (equivalente ao Oracle)
---------------------------------------------------------------
CREATE TABLE dbo.marcador_aruco_movel (
  id_marcador_movel  INT           NOT NULL PRIMARY KEY,
  codigo_aruco       NVARCHAR(50)  NULL,
  data_instalacao    DATE          NULL,
  moto_id_moto       INT           NULL,
  CONSTRAINT marcador_movel_moto_FK FOREIGN KEY (moto_id_moto) REFERENCES dbo.moto(id_moto)
);

ALTER TABLE dbo.marcador_aruco_movel
  ADD CONSTRAINT DF_MARCADOR_MOVEL_ID
  DEFAULT NEXT VALUE FOR dbo.SEQ_MARCADOR_M_CH FOR id_marcador_movel;

---------------------------------------------------------------
-- MEDICAO_POSICAO (equivalente ao Oracle)
---------------------------------------------------------------
CREATE TABLE dbo.medicao_posicao (
  id_medicao                              INT    NOT NULL PRIMARY KEY,
  distancia_m                              FLOAT  NULL,
  posicao_id_posicao                       INT    NULL,
  marcador_fixo_id_marcador_aruco_fixo     INT    NULL,
  CONSTRAINT medicao_posicao_posicao_FK FOREIGN KEY (posicao_id_posicao) REFERENCES dbo.posicao(id_posicao),
  CONSTRAINT medicao_posicao_marcador_fixo_FK FOREIGN KEY (marcador_fixo_id_marcador_aruco_fixo) REFERENCES dbo.marcador_fixo(id_marcador_aruco_fixo)
);

ALTER TABLE dbo.medicao_posicao
  ADD CONSTRAINT DF_MEDICAO_ID
  DEFAULT NEXT VALUE FOR dbo.SEQ_MEDICAO_CH FOR id_medicao;
