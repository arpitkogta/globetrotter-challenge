FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY target/*.jar globetrotter-main.jar
EXPOSE ${PORT}
CMD ["java", "-jar", "globetrotter-main.jar"]