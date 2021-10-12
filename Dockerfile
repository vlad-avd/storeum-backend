FROM openjdk:17-alpine
COPY --chown=10001:10001 build/libs/storeum-backend.jar /app/
WORKDIR /app
CMD ["java", "-jar", "/app/storeum-backend.jar"]