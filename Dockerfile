FROM openjdk:19-jdk-slim
WORKDIR /app
COPY target/security-system-0.0.1-SNAPSHOT.jar security-system-0.0.1-SNAPSHOT.jar
EXPOSE 8080
CMD ["java", "-jar", "security-system-0.0.1-SNAPSHOT.jar"]