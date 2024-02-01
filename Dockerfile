FROM amazoncorretto:8-alpine3.17-jdk
COPY build/libs/us-server-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]