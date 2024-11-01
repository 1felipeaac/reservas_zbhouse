#Imagem Base
FROM openjdk:21-jdk-slim
#Criar usuario spring
RUN addgroup --system spring && adduser --system spring --ingroup spring
#Usar usuario spring ao invés do root
USER spring:spring
#Usar nome do arquivo do build na variavel criada
ARG JAR_FILE=target/*.jar
#Copiar o arquivo para imagem
COPY ${JAR_FILE} app.jar
# Expõe a porta em que a aplicação Spring Boot está rodando
EXPOSE 8080
#Comando para executar no container
ENTRYPOINT ["java", "-jar", "/app.jar"]

