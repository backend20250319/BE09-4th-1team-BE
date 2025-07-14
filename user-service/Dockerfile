# --- 빌드 스테이지 ---
FROM gradle:jdk17 AS builder

# 작업 디렉토리 설정
WORKDIR /app

# Gradle Wrapper 및 빌드 스크립트 복사 (의존성 캐싱 최적화)
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

# 의존성 다운로드 (변경된 파일이 없으면 이 단계는 캐시됨)
RUN ./gradlew dependencies

# 소스 코드 복사 및 JAR 빌드
COPY src src
RUN ./gradlew bootJar

# --- 최종 애플리케이션 스테이지 ---
FROM openjdk:17-jdk-slim

# 빌드 스테이지에서 생성된 JAR 파일 복사
COPY --from=builder /app/build/libs/*.jar app.jar

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "/app.jar"]