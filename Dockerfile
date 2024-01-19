FROM maven:3.9.0-eclipse-temurin-19 AS build
COPY . .
RUN mvn clean package
FROM openjdk:19-jdk
COPY --from=build /target/TAM-tam.jar app.jar
EXPOSE 8080
ENTRYPOINT
["java", "-jar", "app.jar"]
