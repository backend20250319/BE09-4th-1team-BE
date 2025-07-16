FROM openjdk:17-jdk-slim
WORKDIR /app
COPY build/libs/comment-service.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]