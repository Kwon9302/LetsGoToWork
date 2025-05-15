FROM openjdk:21
WORKDIR /app

# 로그 디렉토리 미리 생성
RUN mkdir -p /app/logs

COPY build/libs/NginxKafka-0.0.1-SNAPSHOT.jar app.jar

COPY src/main/resources/ /app/resources/

ENV SPRING_DOCKER_COMPOSE_ENABLED=false

ENTRYPOINT ["java", "-Dlogging.file.path=/app/logs", "-jar", "/app/app.jar"]