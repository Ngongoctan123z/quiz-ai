FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

# Copy Maven wrapper và pom.xml trước để cache dependencies
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Cấp quyền thực thi cho mvnw
RUN chmod +x ./mvnw

# Download dependencies
RUN ./mvnw dependency:go-offline -B

# Copy source code
COPY src src

# Build application
RUN ./mvnw clean package -DskipTests

# Expose port
EXPOSE 8080

# Run application
CMD ["java", "-jar", "target/quiz-ai-0.0.1-SNAPSHOT.jar"]

