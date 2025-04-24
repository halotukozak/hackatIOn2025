# Build stage
FROM gradle:8.3.0-jdk17 AS build
WORKDIR /backend
COPY . .
RUN gradle assemble --no-daemon

# Run stage
FROM openjdk:17-slim
ENV DEPLOYMENT=DEPLOYMENT
WORKDIR /backend
COPY --from=build /backend/build/libs/roomie-all.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
