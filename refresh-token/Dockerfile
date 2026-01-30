FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY refresh-token-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 9306

ENTRYPOINT ["java", "-jar", "app.jar"]
