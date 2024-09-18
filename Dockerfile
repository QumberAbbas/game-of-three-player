FROM amazoncorretto:21 as build

USER root

ENV APP_HOME=/workspace/app

WORKDIR $APP_HOME

# Copy all the files
COPY ./build.gradle.kts ./gradlew ./gradlew.bat $APP_HOME
COPY gradle $APP_HOME/gradle
COPY ./buildSrc $APP_HOME/buildSrc/
COPY ./src $APP_HOME/src/

# Build desirable JAR
RUN ./gradlew clean build -x test

### Final docker image, containing only the necessary for deploy with minimal and optimal layers
FROM amazoncorretto:21

VOLUME /tmp

ARG APP_HOME=/workspace/app

RUN mkdir /app

COPY --from=build $APP_HOME/build/libs/*.jar /app

ENTRYPOINT ["/bin/bash", "-c", "java $JAVA_OPTS -jar /app/*.jar de.lieferando.gameofthree.GameOfThreePlayerApplication"]