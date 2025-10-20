#!/usr/bin/env bash

set -euo pipefail
set +H 2>/dev/null || true

# -----------------------------------------------------
# Cores para feedback visual
# -----------------------------------------------------
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m'

# -----------------------------------------------------
# Descobrir diretórios importantes
# -----------------------------------------------------
SCRIPT_PATH="${BASH_SOURCE[0]:-$0}"
SCRIPT_DIR="$(cd "$(dirname "$SCRIPT_PATH")" && pwd)"
REPO_ROOT="$(cd "${SCRIPT_DIR}/.." && pwd)"
CHALLENGE_DIR="${REPO_ROOT}/challenge-api"
PARENT_DIR="$(cd "${REPO_ROOT}/.." && pwd)"

CREDENTIALS_CANDIDATES=(
  "${REPO_ROOT}/credentials.txt"
  "${PARENT_DIR}/credentials.txt"
)

CREDENTIALS_FILE=""
for candidate in "${CREDENTIALS_CANDIDATES[@]}"; do
  if [ -f "$candidate" ]; then
    CREDENTIALS_FILE="$candidate"
    break
  fi
done

if [ -z "$CREDENTIALS_FILE" ]; then
  echo -e "${RED}❌ Arquivo credentials.txt não encontrado.${NC}"
  echo "Execute primeiro: ./scripts/azure-create-resources.sh"
  exit 1
fi

read_credential() {
  local key="$1"
  awk -F': ' -v target="$key" '$1 == target {print $2; exit}' "$CREDENTIALS_FILE" | tr -d '\r'
}

SQL_SERVER="$(read_credential "Server")"
SQL_DB="$(read_credential "Database")"
SQL_ADMIN="$(read_credential "Username")"
SQL_PASSWORD="$(read_credential "Password")"

if [ -z "$SQL_SERVER" ] || [ -z "$SQL_DB" ] || [ -z "$SQL_ADMIN" ] || [ -z "$SQL_PASSWORD" ]; then
  echo -e "${RED}❌ Não foi possível extrair todas as credenciais do arquivo ${CREDENTIALS_FILE}.${NC}"
  exit 1
fi

SERVER_HOST_PORT="${SQL_SERVER#tcp:}"
SERVER_HOST="$SERVER_HOST_PORT"
SERVER_PORT="1433"

if [[ "$SERVER_HOST_PORT" == *","* ]]; then
  SERVER_PORT="${SERVER_HOST_PORT##*,}"
  SERVER_HOST="${SERVER_HOST_PORT%%,*}"
elif [[ "$SERVER_HOST_PORT" == *":"* ]]; then
  POTENTIAL_PORT="${SERVER_HOST_PORT##*:}"
  if [[ "$POTENTIAL_PORT" =~ ^[0-9]+$ ]]; then
    SERVER_PORT="$POTENTIAL_PORT"
    SERVER_HOST="${SERVER_HOST_PORT%:*}"
  fi
fi

echo -e "${BLUE}================================================${NC}"
echo -e "${BLUE}  CONFIGURAÇÃO DO BANCO DE DADOS (FLYWAY)${NC}"
echo -e "${BLUE}================================================${NC}"
echo ""
echo -e "${YELLOW}🗄️  Server:   ${SERVER_HOST}:${SERVER_PORT}${NC}"
echo -e "${YELLOW}📊 Database: ${SQL_DB}${NC}"
echo ""

# -----------------------------------------------------
# Preparar mvnw
# -----------------------------------------------------
MVNW_SH="${CHALLENGE_DIR}/mvnw"
MVNW_CMD="${CHALLENGE_DIR}/mvnw.cmd"

if [ -f "$MVNW_SH" ]; then
  MVNW="$MVNW_SH"
elif [ -f "$MVNW_CMD" ]; then
  MVNW="$MVNW_CMD"
else
  echo -e "${RED}❌ Wrapper Maven (mvnw) não encontrado em ${CHALLENGE_DIR}.${NC}"
  exit 1
fi

if [ -x "$MVNW" ]; then
  true
else
  chmod +x "$MVNW" 2>/dev/null || true
fi

# -----------------------------------------------------
# Executar migrações Flyway
# -----------------------------------------------------
FLYWAY_URL="jdbc:sqlserver://${SERVER_HOST}:${SERVER_PORT};database=${SQL_DB};encrypt=true;trustServerCertificate=true;loginTimeout=30"

echo -e "${GREEN}🚀 Executando flyway:migrate...${NC}"

pushd "$CHALLENGE_DIR" >/dev/null
"$MVNW" \
  -Dflyway.url="$FLYWAY_URL" \
  -Dflyway.user="$SQL_ADMIN" \
  -Dflyway.password="$SQL_PASSWORD" \
  -Dflyway.locations=filesystem:src/main/resources/db/migration \
  -Dflyway.schemas=dbo \
  -Dflyway.baselineOnMigrate=true \
  flyway:migrate
popd >/dev/null

echo ""
echo -e "${GREEN}✅ Migrações aplicadas com sucesso!${NC}"

# -----------------------------------------------------
# Validação opcional (sqlcmd)
# -----------------------------------------------------
if command -v sqlcmd >/dev/null 2>&1; then
  echo ""
  echo -e "${GREEN}🔍 Validando dados principais...${NC}"
  VALIDATION_QUERY=$'SET NOCOUNT ON;\nSELECT \'patio\' AS tabela, COUNT(*) AS total FROM patio\nUNION ALL\nSELECT \'moto\', COUNT(*) FROM moto\nUNION ALL\nSELECT \'usuario\', COUNT(*) FROM usuario;'
  sqlcmd -S "$SQL_SERVER" -d "$SQL_DB" -U "$SQL_ADMIN" -P "$SQL_PASSWORD" -C -b -Q "$VALIDATION_QUERY" || true
else
  echo ""
  echo -e "${YELLOW}ℹ️  sqlcmd não encontrado. Validação opcional ignorada.${NC}"
fi

echo ""
echo -e "${GREEN}🎉 Banco configurado e pronto para o deploy!${NC}"
echo ""
echo -e "  2.  Faça o deploy: ${BLUE}./scripts/azure-deploy-app.sh${NC}"
echo ""
echo -e "${YELLOW}Os scripts permanecem em ${REPO_ROOT}/database caso deseje consultá-los manualmente.${NC}"
