# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-alpine

# Set the working directory in the container
WORKDIR /app

# Copy the application JAR file to the container
COPY target/ServerExercise-0.0.1-SNAPSHOT.jar /app/ServerExercise-0.0.1-SNAPSHOT.jar

# Expose the port your application runs on
EXPOSE 8574

# Run the application
ENTRYPOINT ["java", "-jar", "/app/ServerExercise-0.0.1-SNAPSHOT.jar"]