# Etapa de construção
FROM maven:3.9.8-eclipse-temurin-17 AS builder

# Copia o código-fonte e o arquivo pom.xml para o contêiner
COPY src /app/src
COPY pom.xml /app

# Define o diretório de trabalho
WORKDIR /app

# Compila e empacota a aplicação, pulando os testes
RUN mvn clean package -DskipTests

# Etapa de execução
FROM eclipse-temurin:17.0.11_9-jre-alpine

# Adiciona um usuário 'spring'
RUN addgroup -g 1000 -S spring && adduser -u 1000 -S spring -G spring
USER spring

# Copia o arquivo JAR gerado na etapa de construção para a etapa de execução
COPY --from=builder /app/target/*.jar /app/app.jar

# Expõe a porta 8080
EXPOSE 8080

# Define o ponto de entrada para rodar a aplicação
ENTRYPOINT ["java", "-jar", "/app/app.jar"]