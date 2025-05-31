## Dockerfile
#FROM maven:3.8.6-eclipse-temurin-17 AS build
#WORKDIR /app
#COPY pom.xml .
#COPY src ./src
#RUN mvn clean package -DskipTests
#
#FROM openjdk:17-jdk-slim
#WORKDIR /app
#COPY --from=build /app/target/school_mate-0.0.1-SNAPSHOT.jar app.jar
#ENTRYPOINT ["java", "-jar", "app.jar"]
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY /target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]