# syntax=docker/dockerfile:1

# ---- Build stage ----
FROM eclipse-temurin:21-jdk AS build
WORKDIR /workspace

# Maven wrapper and pom first (change rarely)
COPY mvnw pom.xml ./
COPY .mvn .mvn

# Source code last (changes often)
COPY src src

# Build the jar; tests run in CI (Testcontainers needs Docker, not available here)
RUN --mount=type=cache,target=/root/.m2 \
    ./mvnw package -DskipTests -B

# Split the jar into layers for better image caching
RUN cp target/*.jar application.jar \
    && java -Djarmode=tools -jar application.jar extract --layers --destination extracted

# ---- Runtime stage ----
FROM eclipse-temurin:21-jre
WORKDIR /app

# Run as an unprivileged system user
RUN groupadd --system jobmappe && useradd --system --gid jobmappe jobmappe

# Copy layers from rarely to frequently changing
COPY --from=build /workspace/extracted/dependencies/ ./
COPY --from=build /workspace/extracted/spring-boot-loader/ ./
COPY --from=build /workspace/extracted/snapshot-dependencies/ ./
COPY --from=build /workspace/extracted/application/ ./

USER jobmappe

# Let the JVM use up to 75 % of the container memory (default is 25 %)
ENV JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=75"

EXPOSE 8080

HEALTHCHECK --interval=10s --timeout=3s --start-period=30s --retries=3 \
  CMD curl -fs -o /dev/null http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "application.jar"]