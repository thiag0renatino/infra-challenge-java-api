#!/bin/bash

set -e  # erros

# Cores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# =====================================================
# CONFIGURAÇÕES
# =====================================================
RESOURCE_GROUP="rg-challenge-mottu"
LOCATION="eastus2"
APP_NAME="app-mottu-api"
SQL_SERVER="sql-mottu-rm556934"
SQL_DB="mottu_db"
SQL_ADMIN="adminmottu"
SQL_PASSWORD="Mottu@Azure2025!Secure"
JWT_SECRET_VALUE="mottu-challenge-fiap-2025-jwt-secret-key-secure"

echo -e "${BLUE}================================================${NC}"
echo -e "${BLUE}  DEPLOY CHALLENGE FIAP 2025 - AZURE SQL${NC}"
echo -e "${BLUE}  Disciplina: DevOps Tools e Cloud Computing${NC}"
echo -e "${BLUE}================================================${NC}"
echo ""

# Verificar se Azure CLI está instalado
if ! command -v az &> /dev/null; then
    echo -e "${RED}❌ Azure CLI não encontrado!${NC}"
    echo "Instale em: https://learn.microsoft.com/cli/azure/install-azure-cli"
    exit 1
fi

# Verificar se está logado
echo -e "${YELLOW}🔐 Verificando autenticação...${NC}"
if ! az account show &> /dev/null; then
    echo -e "${RED}❌ Você não está logado na Azure!${NC}"
    echo "Execute: az login"
    exit 1
fi

echo -e "${GREEN}✅ Autenticado com sucesso!${NC}"
echo ""

# =====================================================
# 1. Criar Resource Group
# =====================================================
echo -e "${GREEN}📦 Criando Resource Group: ${RESOURCE_GROUP}${NC}"
az group create \
  --name $RESOURCE_GROUP \
  --location $LOCATION \
  --output table

echo ""

# =====================================================
# 2. Criar SQL Server
# =====================================================
echo -e "${GREEN}🗄️  Criando SQL Server: ${SQL_SERVER}${NC}"
echo -e "${YELLOW}⏳ Isso pode levar alguns minutos...${NC}"

az sql server create \
  --name $SQL_SERVER \
  --resource-group $RESOURCE_GROUP \
  --location $LOCATION \
  --admin-user $SQL_ADMIN \
  --admin-password $SQL_PASSWORD \
  --output table

echo ""

# =====================================================
# 3. Configurar Firewall do SQL Server
# =====================================================
echo -e "${GREEN}🔥 Configurando firewall do SQL Server...${NC}"

az sql server firewall-rule create \
  -g "$RESOURCE_GROUP" -s "$SQL_SERVER" \
  -n AllowAzureServices --start-ip-address 0.0.0.0 --end-ip-address 0.0.0.0 -o table

MY_IP="$(curl -4 -s ifconfig.me || true)"
if [[ "$MY_IP" =~ ^([0-9]{1,3}\.){3}[0-9]{1,3}$ ]]; then
  az sql server firewall-rule create \
    -g "$RESOURCE_GROUP" -s "$SQL_SERVER" \
    -n AllowMyIP --start-ip-address "$MY_IP" --end-ip-address "$MY_IP" -o table
else
  echo "Aviso: não foi possível detectar IPv4 público. Pulei a regra AllowMyIP."
fi

# =====================================================
# 4. Criar Database
# =====================================================
echo -e "${GREEN}📊 Criando Database: ${SQL_DB}${NC}"
echo -e "${YELLOW}⏳ Isso pode levar alguns minutos...${NC}"

az sql db create \
  --resource-group $RESOURCE_GROUP \
  --server $SQL_SERVER \
  --name $SQL_DB \
  --service-objective Basic \
  --edition Basic \
  --max-size 2GB \
  --backup-storage-redundancy Local \
  --output table

echo ""

# =====================================================
# 5. Criar App Service Plan
# =====================================================
echo -e "${GREEN}📋 Criando App Service Plan...${NC}"

az appservice plan create \
  --name "${APP_NAME}-plan" \
  --resource-group $RESOURCE_GROUP \
  --location $LOCATION \
  --sku B1 \
  --is-linux \
  --output table

echo ""

# =====================================================
# 6. Criar App Service
# =====================================================
echo -e "${GREEN}🌐 Criando App Service: ${APP_NAME}${NC}"

az webapp create \
  --name $APP_NAME \
  --resource-group $RESOURCE_GROUP \
  --plan "${APP_NAME}-plan" \
  --runtime "JAVA:17-java17" \
  --output table

echo ""

# =====================================================
# 7. Configurar Variáveis de Ambiente
# =====================================================
echo -e "${GREEN}⚙️  Configurando variáveis de ambiente...${NC}"

# Montar connection string
JDBC_URL="jdbc:sqlserver://${SQL_SERVER}.database.windows.net:1433;database=${SQL_DB};encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;"

az webapp config appsettings set -n "$APP_NAME" -g "$RESOURCE_GROUP" --settings \
  SPRING_PROFILES_ACTIVE="azure" \
  SPRING_DATASOURCE_URL="jdbc:sqlserver://$SQL_SERVER.database.windows.net:1433;database=$SQL_DB;encrypt=true;trustServerCertificate=true;hostNameInCertificate=*.database.windows.net;loginTimeout=30;" \
  SPRING_DATASOURCE_USERNAME="$SQL_ADMIN@$SQL_SERVER" \
  SPRING_DATASOURCE_PASSWORD="$SQL_PASSWORD" \
  SPRING_DATASOURCE_DRIVER_CLASS_NAME="com.microsoft.sqlserver.jdbc.SQLServerDriver" \
  SPRING_JPA_DATABASE_PLATFORM="org.hibernate.dialect.SQLServerDialect" \
  SECURITY_JWT_TOKEN_SECRET_KEY="$JWT_SECRET_VALUE" \
  CORS_ORIGIN_PATTERNS="*" \
  SPRING_FLYWAY_ENABLED="true" \
  SPRING_FLYWAY_LOCATIONS="classpath:db/migration" \
  SPRING_FLYWAY_SCHEMAS="dbo" \
  SPRING_FLYWAY_BASELINE_ON_MIGRATE="false" \
  SPRING_SQL_INIT_MODE="never" \
  LOGGING_LEVEL_COM_ZAXXER_HIKARI="DEBUG"


echo ""

# =====================================================
# 8. Habilitar Logs
# =====================================================
echo -e "${GREEN}📝 Habilitando logs...${NC}"

az webapp log config \
  --name $APP_NAME \
  --resource-group $RESOURCE_GROUP \
  --application-logging filesystem \
  --level information \
  --detailed-error-messages true \
  --failed-request-tracing true \
  --web-server-logging filesystem \
  --output table

echo ""

# =====================================================
# 9. Salvar credenciais em arquivo
# =====================================================
echo -e "${GREEN}💾 Salvando credenciais...${NC}"

cat > ../credentials.txt << EOF
================================================
CREDENCIAIS DE ACESSO - CHALLENGE FIAP 2025
================================================

🌐 APP SERVICE
URL: https://${APP_NAME}.azurewebsites.net
Swagger: https://${APP_NAME}.azurewebsites.net/swagger-ui/index.html

🗄️ SQL SERVER
Server: ${SQL_SERVER}.database.windows.net
Port: 1433
Database: ${SQL_DB}
Username: ${SQL_ADMIN}
Password: ${SQL_PASSWORD}

📊 CONNECTION STRING
${JDBC_URL}

🔑 JWT SECRET
${JWT_SECRET_VALUE}

================================================
GERADO EM: $(date)
================================================
EOF

echo -e "${GREEN}✅ Credenciais salvas em: ../credentials.txt${NC}"
echo ""

# =====================================================
# ✅ RESUMO
# =====================================================
echo ""
echo -e "${GREEN}================================================${NC}"
echo -e "${GREEN}  ✅ RECURSOS CRIADOS COM SUCESSO!${NC}"
echo -e "${GREEN}================================================${NC}"
echo ""
echo -e "${BLUE}📦 Resource Group:${NC} ${RESOURCE_GROUP}"
echo -e "${BLUE}🗄️  SQL Server:${NC} ${SQL_SERVER}.database.windows.net"
echo -e "${BLUE}📊 Database:${NC} ${SQL_DB}"
echo -e "${BLUE}🌐 App Service:${NC} https://${APP_NAME}.azurewebsites.net"
echo ""
echo -e "${YELLOW}📝 PRÓXIMOS PASSOS:${NC}"
echo -e "  1.  Configure o banco: ${BLUE}./scripts/azure-configure-db.sh${NC}"
echo -e "  2.  Faça o deploy: ${BLUE}./scripts/azure-deploy-app.sh${NC}"
echo ""
