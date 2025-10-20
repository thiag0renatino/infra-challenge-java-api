#!/usr/bin/env bash
set -euo pipefail

# ----------------------------
# Cores
# ----------------------------
RED='\033[0;31m'
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# =====================================================
# VARIÁVEIS
# =====================================================
RG="rg-challenge-mottu"
WEBAPP="app-mottu-api"
MODULE_DIR="challenge-api"
MAVEN_PROFILE=""
SKIP_TESTS=true
DEPLOY_TYPE="jar"
TAIL_LOGS=false
# =====================================================

# ----------------------------
# Resolução de caminhos
# ----------------------------
DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ROOT="$(cd "$DIR/.." && pwd)"
API_DIR="${ROOT}/${MODULE_DIR}"
POM="${API_DIR}/pom.xml"
TARGET_DIR="${API_DIR}/target"

echo -e "${BLUE}==============================================${NC}"
echo -e "${BLUE}  DEPLOY DA API NO AZURE APP SERVICE          ${NC}"
echo -e "${BLUE}==============================================${NC}"
echo ""

# ----------------------------
# Pré-checagens
# ----------------------------
if ! command -v az >/dev/null 2>&1; then
  echo -e "${RED}❌ Azure CLI não encontrado.${NC}"
  echo "Instale: https://learn.microsoft.com/cli/azure/install-azure-cli"
  exit 1
fi
if ! command -v mvn >/dev/null 2>&1; then
  echo -e "${RED}❌ Maven não encontrado no PATH.${NC}"
  echo "Instale: https://maven.apache.org/download.cgi"
  exit 1
fi

echo -e "${YELLOW}🔐 Verificando autenticação Azure...${NC}"
if ! az account show >/dev/null 2>&1; then
  echo -e "${RED}❌ Você não está logado. Execute: az login${NC}"
  exit 1
fi
echo -e "${GREEN}✅ Autenticado!${NC}"

echo -e "${YELLOW}🔎 Validando recursos...${NC}"
if ! az group show -n "$RG" >/dev/null 2>&1; then
  echo -e "${RED}❌ Resource Group '${RG}' não existe.${NC}"
  exit 1
fi
if ! az webapp show -g "$RG" -n "$WEBAPP" >/dev/null 2>&1; then
  echo -e "${RED}❌ WebApp '${WEBAPP}' não encontrado no RG '${RG}'.${NC}"
  exit 1
fi
echo -e "${GREEN}✅ Recursos encontrados.${NC}"
echo ""

# ----------------------------
# Build Maven
# ----------------------------
echo -e "${GREEN}🏗️  [BUILD] Empacotando Maven (${MODULE_DIR})...${NC}"

MVN_FLAGS=(-q clean package -f "$POM")
$SKIP_TESTS && MVN_FLAGS+=(-DskipTests)
[[ -n "$MAVEN_PROFILE" ]] && MVN_FLAGS+=(-P "$MAVEN_PROFILE")

mvn "${MVN_FLAGS[@]}" || {
  echo -e "${RED}❌ Falha no build Maven.${NC}"
  exit 1
}

# ----------------------------
# Localiza artefato
# ----------------------------
ARTIFACT=""
case "$DEPLOY_TYPE" in
  jar)
    if compgen -G "${TARGET_DIR}/*.jar" > /dev/null; then
      ARTIFACT="$(ls -t "${TARGET_DIR}"/*.jar | head -n1)"
    fi
    ;;
  zip)
    # se usar webapp deploy via zip
    if compgen -G "${TARGET_DIR}/*.zip" > /dev/null; then
      ARTIFACT="$(ls -t "${TARGET_DIR}"/*.zip | head -n1)"
    else
      # cria um zip com o conteúdo do target (opcional)
      ARTIFACT="${TARGET_DIR}/app.zip"
      (cd "$TARGET_DIR" && zip -qr "app.zip" .)
    fi
    ;;
  *)
    echo -e "${RED}❌ DEPLOY_TYPE inválido: ${DEPLOY_TYPE} (use 'jar' ou 'zip').${NC}"
    exit 1
    ;;
esac

if [[ -z "${ARTIFACT}" || ! -f "$ARTIFACT" ]]; then
  echo -e "${RED}❌ Artefato não encontrado em ${TARGET_DIR} (tipo=${DEPLOY_TYPE}).${NC}"
  exit 1
fi

echo -e "${YELLOW}📦 Artefato:${NC} ${ARTIFACT}"
echo ""

# ----------------------------
# Deploy
# ----------------------------
echo -e "${GREEN}🚀 [DEPLOY] Publicando no App Service '${WEBAPP}'...${NC}"
if [[ "$DEPLOY_TYPE" == "jar" ]]; then
  az webapp deploy -g "$RG" -n "$WEBAPP" --type jar --src-path "$ARTIFACT" -o table
else
  az webapp deploy -g "$RG" -n "$WEBAPP" --type zip --src-path "$ARTIFACT" -o table
fi

echo -e "${YELLOW}🔄 Reiniciando WebApp...${NC}"
az webapp restart -g "$RG" -n "$WEBAPP" >/dev/null

HOST="$(az webapp show -g "$RG" -n "$WEBAPP" --query defaultHostName -o tsv)"
APP_URL="https://${HOST}"

echo ""
echo -e "${GREEN}==============================================${NC}"
echo -e "${GREEN}  ✅ DEPLOY CONCLUÍDO${NC}"
echo -e "${GREEN}==============================================${NC}"
echo -e "${BLUE}🌐 URL:${NC}     ${APP_URL}"
echo -e "${BLUE}📜 Swagger:${NC} ${APP_URL}/swagger-ui/index.html"
echo -e "${BLUE}🪵 Logs:${NC}   az webapp log tail -g ${RG} -n ${WEBAPP}"
echo ""


