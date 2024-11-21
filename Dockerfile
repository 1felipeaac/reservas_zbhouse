FROM ubuntu:latest AS build

RUN apt-get update

RUN apt-get install openjdk-21-jdk -y

COPY . .

RUN apt-get install maven -y

RUN mvn clean install

#Imagem Base
FROM openjdk:21-jdk-slim
##Criar usuario spring
#RUN addgroup --system spring && adduser --system spring --ingroup spring
##Usar usuario spring ao invés do root
#USER spring:spring
##Usar nome do arquivo do build na variavel criada
#ARG JAR_FILE=target/*.jar
##Copiar o arquivo para imagem
#COPY ${JAR_FILE} app.jar
## Expõe a porta em que a aplicação Spring Boot está rodando
EXPOSE 8080
#Comando para executar no container
COPY --from=build /target/zbhousereservas-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]

