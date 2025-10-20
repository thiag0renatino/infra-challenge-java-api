-- Tabela: PATIO
CREATE TABLE dbo.patio (
  id_patio INT NOT NULL PRIMARY KEY,
  nome NVARCHAR(100) NULL,
  localizacao NVARCHAR(100) NOT NULL,
  descricao NVARCHAR(255) NULL
);
GO

-- Sequence: SEQ_PATIO_CH (START WITH 6)
CREATE SEQUENCE dbo.SEQ_PATIO_CH START WITH 6 INCREMENT BY 1;
GO
ALTER TABLE dbo.patio ADD CONSTRAINT DF_PATIO_ID DEFAULT NEXT VALUE FOR dbo.SEQ_PATIO_CH FOR id_patio;
GO

-- Tabela: USUARIO
CREATE TABLE dbo.usuario (
  id_usuario INT NOT NULL PRIMARY KEY,
  nome NVARCHAR(100) NOT NULL,
  email NVARCHAR(100) NOT NULL UNIQUE,
  senha NVARCHAR(255) NOT NULL,
  status NVARCHAR(20) CONSTRAINT DF_USUARIO_STATUS DEFAULT 'ativo',
  tipo NVARCHAR(20) NULL,
  patio_id_patio INT NOT NULL,
  CONSTRAINT CK_USUARIO_TIPO CHECK (tipo IN ('USER','ADMIN')),
  CONSTRAINT usuario_patio_FK FOREIGN KEY (patio_id_patio) REFERENCES dbo.patio(id_patio)
);
GO

-- Sequence: SEQ_USUARIO_CH (START WITH 6)
CREATE SEQUENCE dbo.SEQ_USUARIO_CH START WITH 6 INCREMENT BY 1;
GO
ALTER TABLE dbo.usuario ADD CONSTRAINT DF_USUARIO_ID DEFAULT NEXT VALUE FOR dbo.SEQ_USUARIO_CH FOR id_usuario;
GO

-- Tabela: MOTO
CREATE TABLE dbo.moto (
  id_moto INT NOT NULL PRIMARY KEY,
  placa NVARCHAR(7) NULL UNIQUE,
  modelo NVARCHAR(50) NULL,
  status NVARCHAR(65) NULL,
  data_cadastro DATE NULL
);
GO

-- Sequence: SEQ_MOTO_CH (START WITH 9)
CREATE SEQUENCE dbo.SEQ_MOTO_CH START WITH 9 INCREMENT BY 1;
GO
ALTER TABLE dbo.moto ADD CONSTRAINT DF_MOTO_ID DEFAULT NEXT VALUE FOR dbo.SEQ_MOTO_CH FOR id_moto;
GO

-- Tabela: POSICAO
CREATE TABLE dbo.posicao (
  id_posicao INT NOT NULL PRIMARY KEY,
  x_pos FLOAT NULL,
  y_pos FLOAT NULL,
  data_hora DATETIME2 NULL,
  moto_id_moto INT NULL,
  patio_id_patio INT NULL,
  CONSTRAINT posicao_moto_FK  FOREIGN KEY (moto_id_moto) REFERENCES dbo.moto(id_moto),
  CONSTRAINT posicao_patio_FK FOREIGN KEY (patio_id_patio) REFERENCES dbo.patio(id_patio)
);
GO

-- Sequence: SEQ_POS_CH (START WITH 10)
CREATE SEQUENCE dbo.SEQ_POS_CH START WITH 10 INCREMENT BY 1;
GO
ALTER TABLE dbo.posicao ADD CONSTRAINT DF_POSICAO_ID DEFAULT NEXT VALUE FOR dbo.SEQ_POS_CH FOR id_posicao;
GO

-- Tabela: MARCADOR_FIXO
CREATE TABLE dbo.marcador_fixo (
  id_marcador_aruco_fixo INT NOT NULL PRIMARY KEY,
  codigo_aruco NVARCHAR(50) NULL,
  x_pos FLOAT NULL,
  y_pos FLOAT NULL,
  patio_id_patio INT NULL,
  CONSTRAINT marcador_fixo_patio_FK FOREIGN KEY (patio_id_patio) REFERENCES dbo.patio(id_patio)
);
GO

-- Sequence: SEQ_MARCADOR_F_CH (START WITH 16)
CREATE SEQUENCE dbo.SEQ_MARCADOR_F_CH START WITH 16 INCREMENT BY 1;
GO
ALTER TABLE dbo.marcador_fixo ADD CONSTRAINT DF_MARCADOR_FIXO_ID DEFAULT NEXT VALUE FOR dbo.SEQ_MARCADOR_F_CH FOR id_marcador_aruco_fixo;
GO

-- Tabela: MARCADOR_ARUCO_MOVEL
CREATE TABLE dbo.marcador_aruco_movel (
  id_marcador_movel INT NOT NULL PRIMARY KEY,
  codigo_aruco NVARCHAR(50) NULL,
  data_instalacao DATE NULL,
  moto_id_moto INT NULL,
  CONSTRAINT marcador_movel_moto_FK FOREIGN KEY (moto_id_moto) REFERENCES dbo.moto(id_moto)
);
GO

-- Sequence: SEQ_MARCADOR_M_CH (START WITH 6)
CREATE SEQUENCE dbo.SEQ_MARCADOR_M_CH START WITH 6 INCREMENT BY 1;
GO
ALTER TABLE dbo.marcador_aruco_movel ADD CONSTRAINT DF_MARCADOR_MOVEL_ID DEFAULT NEXT VALUE FOR dbo.SEQ_MARCADOR_M_CH FOR id_marcador_movel;
GO

-- Tabela: MEDICAO_POSICAO
CREATE TABLE dbo.medicao_posicao (
  id_medicao INT NOT NULL PRIMARY KEY,
  distancia_m FLOAT NULL,
  posicao_id_posicao INT NULL,
  marcador_fixo_id_marcador_aruco_fixo INT NULL,
  CONSTRAINT medicao_posicao_posicao_FK FOREIGN KEY (posicao_id_posicao) REFERENCES dbo.posicao(id_posicao),
  CONSTRAINT medicao_posicao_marcador_FK FOREIGN KEY (marcador_fixo_id_marcador_aruco_fixo) REFERENCES dbo.marcador_fixo(id_marcador_aruco_fixo)
);
GO

-- Sequence: SEQ_MEDICAO_CH (START WITH 26)
CREATE SEQUENCE dbo.SEQ_MEDICAO_CH START WITH 26 INCREMENT BY 1;
GO
ALTER TABLE dbo.medicao_posicao ADD CONSTRAINT DF_MEDICAO_ID DEFAULT NEXT VALUE FOR dbo.SEQ_MEDICAO_CH FOR id_medicao;
GO
