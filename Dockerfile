# Build stage
FROM maven:3-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Run stage
FROM openjdk:17-jdk-slim
WORKDIR /app

# Install dependencies for Cloud SQL proxy and required libraries
RUN apt-get update && apt-get install -y \
    curl \
    && curl -sSL https://dl.google.com/cloudsql/cloud_sql_proxy.linux.amd64 -o /usr/local/bin/cloud_sql_proxy \
    && chmod +x /usr/local/bin/cloud_sql_proxy

# Copy the built JAR file from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose the port
EXPOSE 8080
ENV PORT=8080

# Set up the Cloud SQL connection for PostgreSQL (using the instance connection name)
# Run both Cloud SQL proxy and Spring Boot app
ENTRYPOINT ["/bin/sh", "-c", "cloud_sql_proxy -dir=/cloudsql -instances=loyal-polymer-447814-a4:europe-west3:tiered-web-app-db-a69a & java -XX:+ExitOnOutOfMemoryError -Djava.security.egd=file:/dev/./urandom -jar app.jar"]
