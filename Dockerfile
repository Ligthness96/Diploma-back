FROM gradle:7-jdk11-slim AS build
WORKDIR /home/gradle/src
COPY . .
RUN gradle buildFatJar --no-daemon

FROM openjdk:11-jre-slim
EXPOSE 8080
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/*.jar /app/diploma-back.jar
ENTRYPOINT ["java", "-jar", "/app/diploma-back.jar"]
