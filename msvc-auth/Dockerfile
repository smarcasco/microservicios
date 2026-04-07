# Stage 1: Build
FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app

# Configura el settings.xml antes de ejecutar Maven
COPY .m2/settings.xml /root/.m2/settings.xml
COPY pom.xml .

# Copy source code
COPY src ./src

# Build the application
RUN mvn -s /root/.m2/settings.xml clean package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copy the JAR from build stage
COPY --from=build /app/target/*.jar app.jar

# Expose port
EXPOSE 9000

# Set environment variables
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]

