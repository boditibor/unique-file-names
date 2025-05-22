FROM eclipse-temurin:21-jre
WORKDIR /app
COPY build/libs/unique-file-names-0.0.1-SNAPSHOT.jar app.jar
COPY build/docs/javadoc /app/build/docs/javadoc
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
