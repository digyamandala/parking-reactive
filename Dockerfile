FROM openjdk:8-jdk-alpine
COPY parking-reactive-app/target/parking-reactive-app-0.1.0-1-SNAPSHOT.jar /docker-app/parking-reactive-app.jar

CMD ["java", "-jar", "/docker-app/parking-reactive-app.jar"]
