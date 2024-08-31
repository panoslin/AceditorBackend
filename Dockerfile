# Stage 1: Build
FROM maven:3-eclipse-temurin-22-alpine as build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Stage 2: Run
FROM eclipse-temurin:22-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/AceditorBackend-0.0.1-SNAPSHOT.jar /app/AceditorBackend.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/AceditorBackend.jar", "--spring.profiles.active=prod"]