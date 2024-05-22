FROM openjdk:11-jdk

EXPOSE 8080

RUN apt-get update && apt-get install -y zip

RUN apt-get update && apt-get install -y \
    curl \
    && curl -s https://get.sdkman.io | bash \
    && bash -c "source $HOME/.sdkman/bin/sdkman-init.sh && sdk install kotlin"

WORKDIR /app

COPY . /app

RUN chmod +x gradlew

RUN ./gradlew build

CMD ["bash", "-c", "source $HOME/.sdkman/bin/sdkman-init.sh && kotlin", "src/main/kotlin/tihonin/sergey/Application.kt"]
