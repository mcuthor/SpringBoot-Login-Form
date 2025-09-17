# Stage 1: Build the application using Maven
FROM maven:3.9.9-eclipse-temurin-21 AS build

# Set the working directory
WORKDIR /app

# Copy only the backend project into the container
COPY springbootBackend-main/ .

# Build the project
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM openjdk:21-slim

WORKDIR /app

# Copy only the JAR file
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
