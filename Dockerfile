# temp build
FROM docker.io/gradle:8.0.2-jdk AS TEMP_BUILD
ARG SKIP_TESTS=false
# Copy project files
COPY --chown=gradle:gradle build.gradle settings.gradle /home/gradle/src/
COPY --chown=gradle:gradle src /home/gradle/src/src
COPY --chown=gradle:gradle gradle /home/gradle/src/gradle
WORKDIR /home/gradle/src
RUN if [ "$SKIP_TESTS" = "true" ]; then \
    gradle build --no-daemon -x test; \
  else \
    gradle build --no-daemon; \
  fi

# build image
FROM openjdk:17-alpine
RUN addgroup -S nonroot \
    && adduser -S nonroot -G nonroot
USER nonroot
WORKDIR /app
COPY --from=TEMP_BUILD /home/gradle/src/build/libs/*.jar /app/
ENTRYPOINT ["java", "-jar", "/app/in2-blockchain-connector-0.0.1.jar"]
