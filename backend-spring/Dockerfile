FROM maven:3.6-openjdk-14 AS build
COPY ./src /app/src
COPY pom.xml /app/
WORKDIR /app
RUN mvn -f /app/pom.xml clean package

FROM openjdk:14
ARG JAR_FILE=/app/target/*.jar
COPY --from=build ${JAR_FILE} application.jar
ENTRYPOINT ["java", "-jar", "application.jar"]
EXPOSE 8080