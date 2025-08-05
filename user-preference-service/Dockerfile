FROM --platform=linux/arm64 alpine/java:22-jdk
RUN apk add --no-cache maven
WORKDIR /app
COPY . .
RUN chmod +x ./mvnw
RUN ./mvnw install -DskipTests
ARG SPRING_PROFILES_ACTIVE=dev
ENV SPRING_PROFILES_ACTIVE=${SPRING_PROFILES_ACTIVE}
CMD ["java", "-jar", "target/user-preference-service-0.0.1-SNAPSHOT.jar"]