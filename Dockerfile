FROM gradle:8.9.0-jdk21-jammy AS BUILD
WORKDIR /usr/app
COPY . .
RUN gradle build
RUN gradle jar

FROM bellsoft/liberica-openjre-debian:21-cds

ENV ARTIFACT_NAME=enx-plugin-betterwindcharger-1.0-SNAPSHOT.jar
ENV APP_HOME=/usr/app

WORKDIR $APP_HOME

COPY --from=BUILD $APP_HOME .

RUN mkdir $APP_HOME/server/plugins \
    && cp $APP_HOME/eula.txt $APP_HOME/server/ \
    && cp $APP_HOME/build/libs/$ARTIFACT_NAME $APP_HOME/server/plugins/

EXPOSE 25565
WORKDIR $APP_HOME/server
ENTRYPOINT exec java -jar spigot.jar nogui
