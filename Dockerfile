FROM gradle:8.5.0-jdk17 AS build

WORKDIR /app

COPY build.gradle settings.gradle /app/
COPY src /app/src

# Build the application excluding tests
RUN gradle build -x test --no-daemon

FROM amazoncorretto:17-alpine3.16-jdk

WORKDIR /app

# Copy the built .war file from the build stage
COPY --from=build /app/build/libs/sns-ws-1.0.0.jar /app/sns.jar

# Expose the port that the Spring Boot application will run on
EXPOSE 12013

# Command to run the application
CMD ["java", "-jar", "sns.jar"]
