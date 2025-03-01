FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY target/*.jar globetrotter-main.jar

# Remove EXPOSE since Railway dynamically assigns the port
# EXPOSE ${PORT}  <-- REMOVE THIS

CMD ["java", "-jar", "globetrotter-main.jar", "--server.port=${PORT}"]
