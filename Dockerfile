FROM gradle:8.9.0-jdk21-jammy AS BUILD
WORKDIR /usr/app
COPY . .
RUN gradle shadowJar

FROM bellsoft/liberica-openjre-debian:21-cds AS PROD

ENV ARTIFACT_NAME=pluginenxtest-1.0-SNAPSHOT-all.jar
ENV APP_HOME=/usr/app

WORKDIR $APP_HOME

COPY --from=BUILD $APP_HOME .

RUN mkdir $APP_HOME/server/plugins \
    && cp $APP_HOME/build/libs/$ARTIFACT_NAME $APP_HOME/server/plugins/

EXPOSE 25565
WORKDIR $APP_HOME/server
ENTRYPOINT exec java -jar spigot.jar nogui
