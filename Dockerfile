# ============================================================
# Multi-stage build
#   1) build  : Gradle 래퍼로 실행 가능한 bootJar 생성
#   2) runtime: JRE만 포함한 경량 이미지로 JAR 실행
# ============================================================

# --- 1) build stage ---
FROM eclipse-temurin:21-jdk AS build
WORKDIR /workspace

# 의존성 레이어 캐싱: 빌드 스크립트/래퍼 먼저 복사
COPY gradlew gradlew.bat settings.gradle build.gradle ./
COPY gradle gradle
RUN chmod +x gradlew

# 소스 복사 후 실행 가능 JAR 생성 (테스트는 CI에서 별도 수행)
COPY src src
RUN ./gradlew clean bootJar -x test --no-daemon

# --- 2) runtime stage ---
# 실제 채점(judge.mode=code)을 위해 JDK(javac) + python3 를 포함한다.
FROM eclipse-temurin:21-jdk AS runtime
WORKDIR /app
RUN apt-get update \
    && apt-get install -y --no-install-recommends python3 \
    && rm -rf /var/lib/apt/lists/*
COPY --from=build /workspace/build/libs/*.jar app.jar

EXPOSE 8080
ENV SPRING_PROFILES_ACTIVE=docker
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
