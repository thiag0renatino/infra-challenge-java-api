# Usa imagem leve com Java 17
FROM eclipse-temurin:17-jdk-alpine

# Cria usuário sem privilégios
RUN addgroup -S spring && adduser -S spring -G spring

# Diretório de trabalho dentro do container
WORKDIR /app

# Copia o .jar para dentro da imagem
COPY target/challenge-api-0.0.1-SNAPSHOT.jar app.jar

# Ajusta permissões
RUN chown spring:spring app.jar

# Troca para o usuário limitado
USER spring

# Expõe a porta padrão do Spring Boot
EXPOSE 8080

# Comando para iniciar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
