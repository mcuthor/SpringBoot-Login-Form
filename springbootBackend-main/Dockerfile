# ---------- Build Stage ----------
FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /app

# Copy only the backend project folder (where pom.xml lives)
COPY springbootBackend-main/ .

RUN mvn clean package -DskipTests

# ---------- Runtime Stage ----------
FROM openjdk:21-slim

WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
