# Build stage
FROM maven:3-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Run stage
FROM openjdk:17-jdk-slim
WORKDIR /app

# Install dependencies for Cloud SQL
RUN apt-get update && apt-get install -y \
    curl \
    && curl -sSL https://storage.googleapis.com/cloud-sql-connectors/cloud-sql-proxy/v2.6.1/cloud-sql-proxy.linux.amd64 -o /usr/local/bin/cloud-sql-proxy \
    && chmod +x /usr/local/bin/cloud-sql-proxy

# Copy the built JAR file from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose the port
EXPOSE 8080
ENV PORT=8080

# Set up Cloud SQL connection
ENTRYPOINT ["/bin/sh", "-c", "cloud-sql-proxy --port 5432 loyal-polymer-447814-a4:europe-west3:tiered-web-app-db-a69a & exec java -XX:+ExitOnOutOfMemoryError -Djava.security.egd=file:/dev/./urandom -jar app.jar"]
