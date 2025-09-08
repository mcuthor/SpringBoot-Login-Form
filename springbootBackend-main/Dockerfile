# Start from Java 21 slim image—lean and clean
FROM openjdk:21-slim AS build

WORKDIR /app

# Copy project files and build inside container for reproducibility
COPY . .

# If using Maven
RUN ./mvnw package -DskipTests

# If using Gradle (uncomment instead):
# RUN ./gradlew bootJar --no-daemon

# --- Production image ---
FROM openjdk:21-slim

WORKDIR /app

# Copy the JAR from build stage
ARG JAR_FILE=target/*.jar
# If using Gradle, you might use: ARG JAR_FILE=build/libs/*.jar
COPY --from=build /app/${JAR_FILE} app.jar

# Run as non-root user—safer is smarter
RUN groupadd --system spring && useradd --system --gid spring spring
USER spring:spring

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
