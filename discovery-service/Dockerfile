# --- 빌더 스테이지 (Builder Stage) ---
# 애플리케이션을 빌드하는 데 사용되는 환경입니다.
# 여기서는 Gradle과 OpenJDK 17이 포함된 이미지를 사용합니다.
FROM gradle:jdk17 AS builder

# 작업 디렉토리를 /app으로 설정합니다.
WORKDIR /app

# Gradle Wrapper 파일과 설정 파일을 먼저 복사하여 종속성 캐싱을 활용합니다.
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

# 필요한 경우 Gradle 종속성을 미리 다운로드합니다.
# 이렇게 하면 소스 코드가 변경되어도 종속성이 변경되지 않으면 이 레이어가 캐시되어 빌드 속도가 빨라집니다.
RUN ./gradlew dependencies

# 모든 소스 코드를 복사합니다.
COPY src src

# 애플리케이션을 빌드합니다.
# 'bootJar'는 Spring Boot 애플리케이션의 실행 가능한 JAR 파일을 생성하는 Gradle 태스크입니다.
# Maven을 사용하는 경우 'mvn package' 또는 'mvn clean install' 등을 사용합니다.
RUN ./gradlew bootJar

# --- 런타임 스테이지 (Runtime Stage) ---
# 실제 애플리케이션이 실행될 최종 이미지입니다.
# 빌더 스테이지에서 생성된 JAR 파일만 포함하여 이미지 크기를 최소화합니다.
FROM openjdk:17-jdk-slim

# JAR 파일 경로를 인자로 받습니다. 빌더 스테이지에서 생성된 JAR 파일을 가리킵니다.
ARG JAR_FILE=build/libs/*.jar

# 빌더 스테이지에서 생성된 JAR 파일을 최종 이미지로 복사합니다.
# 'builder' 스테이지에서 /app/build/libs/ 경로에 있는 JAR 파일을 현재 스테이지의 /app.jar로 복사합니다.
COPY --from=builder /app/${JAR_FILE} app.jar

# 애플리케이션을 실행하는 명령어입니다.
# java -jar 명령어를 사용하여 JAR 파일을 실행합니다.
ENTRYPOINT ["java", "-jar", "/app.jar"]
