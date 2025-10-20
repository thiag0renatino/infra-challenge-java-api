# challenge-java-api

> API RESTful e aplicação web para mapeamento e rastreamento de motos nos pátios da MOTTU.

---

## Descrição do Projeto

A ausência de um sistema automatizado de mapeamento e localização das motos compromete a agilidade da operação nos pátios da empresa MOTTU e aumenta o risco de falhas humanas.

Este projeto propõe uma solução tecnológica que visa melhorar a eficiência operacional por meio de:

- **Cálculo preciso da posição das motos** por trilateração, com base em distâncias medidas entre marcadores ArUco fixos e móveis instalados no ambiente (fixos) e nos veículos (móveis);
- **Armazenamento e rastreamento histórico** de posições para auditoria e controle de movimentação;
- **Integração via API RESTful** construída com Java Spring Boot, seguindo boas práticas de arquitetura, DTOs, cache e tratamento de erros;
- **Interface web (Thymeleaf)** para gestão visual dos dados;
- **Versionamento do banco de dados com Flyway**, garantindo consistência e reprodutibilidade em diferentes ambientes.

📌 **Público-alvo:** Funcionários responsáveis pela gestão de pátios da MOTTU.

A API será consumida tanto por uma aplicação móvel quanto pela interface web, que permitem:

- Visualização de um mapa digital com as posições em tempo real das motos (aplicação móvel);
- Consulta interativa: ao clicar sobre uma moto, visualizar informações detalhadas (placa, modelo, status, histórico de localização etc.);
- Operações administrativas via painel web.

---

## Tecnologias e Ferramentas Utilizadas

- **Java 17**
- **Spring Boot 3**
- **Maven**
- **Flyway** 


### Módulos Spring Utilizados
- `Spring Web` – construção da API RESTful
- `Spring Data JPA` – persistência de dados com ORM
- `Spring Validation` – validação de dados de entrada
- `Spring Cache` – melhoria de performance com cache de dados
- `Spring Security` - autenticação e autorização com suporte a JWT
- `MapStruct` – mapeamento automático entre entidades e DTOs
- `Spring Thymeleaf` – templates HTML dinâmicos para a aplicação web

**Segurança & JWT**
- `thymeleaf-extras-springsecurity6` — Integração de segurança nas views
- `com.auth0:java-jwt` — Emissão/validação de tokens JWT

### Persistência de Dados
- **H2 Database** – ambiente de desenvolvimento
- **Oracle Database** – ambiente de produção

### Documentação e Testes da API
- **Swagger / OpenAPI** – documentação interativa da API
- **Swagger UI** – testes de endpoints
- **Postman** – testes manuais

---

## Pré-requisitos

- Java 17+
- Maven 3.8+
- IDE (IntelliJ, Eclipse ou VS Code)
- Banco de dados Oracle ou uso do H2 (perfil dev)

---

## ▶️ Como Executar

```bash
# Clone o repositório
git clone https://github.com/thiag0renatino/challenge-java-api.git
cd challenge-java-api

# Compile o projeto
mvn clean install

# Execute a aplicação
mvn spring-boot:run
```

API RESTful (documentação interativa via Swagger UI):  
👉 `http://localhost:8080/swagger-ui/index.html`

Aplicação Web (interface visual com Thymeleaf):  
👉 `http://localhost:8080/web`

---

## Principais Entidades

| Entidade             | Descrição                                                                |
|----------------------|--------------------------------------------------------------------------|
| `Moto`               | Representa a moto rastreada (placa, modelo, status etc.)                |
| `Posicao`            | Coordenadas x, y de uma moto em um momento específico                   |
| `MarcadorFixo`       | Marcador ArUco fixo instalado no pátio                                  |
| `MarcadorArucoMovel` | Marcador ArUco preso à moto                                            |
| `MedicaoPosicao`     | Distância entre marcador fixo e moto para cálculo de posição            |
| `Usuario`            | Funcionário cadastrado no sistema                                       |
| `Patio`              | Unidade física onde motos são monitoradas                              |

---

## Endpoints da API

### 🔑 Auth Controller
- `POST /auth/signIn` → Autentica o usuário e emite tokens JWT
- `POST /auth/register` → Registra novo usuário
- `PUT /auth/refresh/{email}` → Gera novo token a partir do refresh token

---

### 👤 Usuário Controller
- `GET /api/usuarios` → Listar todos os usuários
- `GET /api/usuarios/{id}` → Buscar usuário por ID
- `GET /api/usuarios/por-email` → Buscar usuário por e-mail
- `POST /api/usuarios` → Criar novo usuário
- `PUT /api/usuarios/{id}` → Atualizar usuário
- `PATCH /api/usuarios/atualizar-senha` → Atualizar senha do usuário
- `DELETE /api/usuarios/{id}` → Excluir usuário

---

### 🏍️ Moto Controller
- `GET /api/motos` → Listar motos com paginação
- `GET /api/motos/{id}` → Buscar moto por ID
- `GET /api/motos/status/{status}` → Listar motos por status
- `GET /api/motos/buscar` → Buscar moto por placa
- `GET /api/motos/{id}/posicoes` → Listar posições da moto
- `POST /api/motos` → Cadastrar nova moto
- `PUT /api/motos/{id}` → Atualizar moto
- `DELETE /api/motos/{id}` → Excluir moto

---

### 📍 Posicao Controller
- `GET /api/posicoes` → Listar todas as posições com paginação
- `GET /api/posicoes/ultimas` → Listar as 10 últimas posições
- `GET /api/posicoes/revisao` → Listar posições de motos em revisão
- `GET /api/posicoes/moto/{motoId}` → Listar posições por ID da moto
- `GET /api/posicoes/historico/{motoId}` → Histórico de posições da moto
- `GET /api/posicoes/{id}/moto` → Buscar moto por posição
- `POST /api/posicoes` → Cadastrar nova posição
- `PUT /api/posicoes/{id}` → Atualizar posição
- `DELETE /api/posicoes/{id}` → Excluir posição

---

### 🏢 Pátio Controller
- `GET /api/patios` → Listar pátios com paginação
- `GET /api/patios/{id}` → Buscar pátio por ID
- `GET /api/patios/{id}/posicoes` → Listar posições registradas no pátio
- `GET /api/patios/{id}/motos` → Listar motos com histórico no pátio
- `GET /api/patios/com-motos` → Listar pátios com motos
- `POST /api/patios` → Cadastrar novo pátio
- `PUT /api/patios/{id}` → Atualizar pátio
- `DELETE /api/patios/{id}` → Excluir pátio

---

### 🎯 Medição de Posição Controller
- `GET /api/medicoes` → Listar todas as medições com paginação
- `GET /api/medicoes/{id}` → Buscar medição por ID
- `GET /api/medicoes/posicao/{id}` → Listar medições por posição
- `GET /api/medicoes/posicao/{id}/contagem` → Contar medições por posição
- `GET /api/medicoes/marcador/{id}` → Listar medições por marcador fixo
- `POST /api/medicoes` → Cadastrar nova medição
- `DELETE /api/medicoes/{id}` → Excluir medição

---

### 📡 Marcador ArUco Móvel Controller
- `GET /api/marcadores-moveis` → Listar todos os marcadores móveis
- `GET /api/marcadores-moveis/{id}` → Buscar marcador móvel por ID
- `GET /api/marcadores-moveis/por-moto` → Buscar marcador por ID da moto
- `GET /api/marcadores-moveis/por-codigo` → Buscar marcador por código ArUco
- `POST /api/marcadores-moveis` → Cadastrar novo marcador móvel
- `PUT /api/marcadores-moveis/{id}` → Atualizar marcador móvel
- `DELETE /api/marcadores-moveis/{id}` → Excluir marcador móvel

---

### 🗺️ Marcador Fixo Controller
- `GET /api/marcadores-fixos` → Listar marcadores fixos com paginação
- `GET /api/marcadores-fixos/por-patio/{patioId}` → Listar por pátio
- `GET /api/marcadores-fixos/por-codigo` → Buscar por código ArUco
- `POST /api/marcadores-fixos` → Cadastrar novo marcador fixo
- `DELETE /api/marcadores-fixos/{id}` → Excluir marcador fixo

---

## Banco de Dados

- **H2 em memória (dev)**
- **Oracle (produção)**
- Configurações no arquivo `application.properties`
- **Migrações controladas pelo Flyway**

---

## Alunos

- Thiago Renatino Paulino — RM556934
- Cauan Matos Moura — RM558821
- Gustavo Roberto — RM558033

---

## Licença

Projeto acadêmico – FIAP & MOTTU Challenge 2025
