FROM ubuntu:latest AS build

RUN apt-get update && apt-get install -y openjdk-21-jdk maven

WORKDIR /app

COPY . .

RUN /bin/sh -c "mvn clean install -Dmaven.compiler.release=21"

FROM openjdk:21-jdk-slim

EXPOSE 8080

COPY --from=build /app/target/zbhousereservas-0.0.1-SNAPSHOT.jar /app/app.jar

WORKDIR /app

ENTRYPOINT ["java", "-jar", "app.jar"]