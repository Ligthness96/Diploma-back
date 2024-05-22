FROM openjdk:11-jdk
COPY . /app
WORKDIR /app
RUN ./gradlew build
CMD ["java", "-jar", "build/libs/your-app.jar"]
