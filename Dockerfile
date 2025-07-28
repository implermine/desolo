# ===========================================
# Build Stage
# ===========================================
FROM gradle:8.5-jdk17-alpine AS build

WORKDIR /app

# Copy gradle files for dependency caching
COPY build.gradle.kts settings.gradle.kts ./
COPY gradle gradle

# Download dependencies (this layer will be cached)
RUN gradle dependencies --no-daemon

# Copy source code
COPY src src

# Build application
RUN gradle build -x test --no-daemon

# ===========================================
# Runtime Stage
# ===========================================
FROM openjdk:17-jdk-alpine

# Install curl for health checks
RUN apk add --no-cache curl

# Create non-root user
RUN addgroup -g 1001 -S appgroup && \
    adduser -u 1001 -S appuser -G appgroup

WORKDIR /app

# Copy built jar from build stage
COPY --from=build /app/build/libs/*.jar app.jar

# Change ownership to non-root user
RUN chown -R appuser:appgroup /app

# Switch to non-root user
USER appuser

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

# Run application
ENTRYPOINT ["java", "-jar", "app.jar"] 