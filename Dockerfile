# ---- Build stage ----
FROM eclipse-temurin:26-jdk AS build
WORKDIR /app

COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
COPY src/ src/

# Cache the Maven repository between builds (BuildKit cache mount).
RUN --mount=type=cache,target=/root/.m2 ./mvnw -B package -DskipTests

# ---- Runtime stage ----
FROM eclipse-temurin:26-jre
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

# Fit within small containers (Render free tier: 512 MB).
ENV JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=75.0"

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
