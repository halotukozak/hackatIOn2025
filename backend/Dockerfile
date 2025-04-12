# Build stage
FROM gradle:8.3.0-jdk17 AS build
WORKDIR /
COPY . .
RUN gradle build --no-daemon

# Run stage
FROM openjdk:17-slim
WORKDIR /
COPY --from=build /build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
