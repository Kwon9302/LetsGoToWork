FROM openjdk:21
WORKDIR /app
COPY build/libs/NginxKafka-0.0.1-SNAPSHOT.jar app.jar
ENV SPRING_DOCKER_COMPOSE_ENABLED=false
COPY src/main/resources/templates /app/resources/templates
ENTRYPOINT ["java", "-jar", "/app/app.jar"]