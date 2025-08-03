FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY . .
RUN ./mvnw install -DskipTests
CMD ["java", "-jar", "target/tracking-service-0.0.1-SNAPSHOT.jar"]