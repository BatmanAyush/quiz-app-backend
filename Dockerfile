# --- Build Stage ---
# Use an official Maven image with Java 21 to build the application.
FROM maven:3.9.6-eclipse-temurin-21 AS builder

# Set the working directory inside the container.
WORKDIR /app

# Copy the pom.xml file first to leverage Docker's layer caching.
COPY pom.xml .

# Download all the project dependencies.
RUN mvn dependency:go-offline

# Copy the rest of your source code into the container.
COPY src ./src

# Package the application into an executable JAR file.
RUN mvn package -DskipTests


# --- Run Stage ---
# Use a slim, production-ready Java 21 JRE image.
FROM eclipse-temurin:21-jre-jammy

# Set the working directory.
WORKDIR /app

# Copy the executable JAR file from the 'builder' stage.
COPY --from=builder /app/target/tool-0.0.1-SNAPSHOT.jar app.jar

# Expose the port that your Spring Boot application runs on (e.g., 8081).
EXPOSE 8081

# The command to run your application when the container starts.
ENTRYPOINT ["java", "-jar", "app.jar"]
