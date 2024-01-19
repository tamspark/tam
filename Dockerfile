FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
COPY target/*.jar app.jar
ENV MY_SECRET_KEY=secret
ENTRYPOINT ["java","-jar","/app.jar"]
