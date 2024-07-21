FROM gradle:8.9.0-jdk21-jammy AS BUILD
WORKDIR /usr/app
COPY . .
RUN gradle build

FROM bellsoft/liberica-openjre-debian:21-cds
ENV ARTIFACT_NAME=mc-plugin-better_wind_charger-0.1.jar
ENV APP_HOME=/usr/app
WORKDIR $APP_HOME
COPY --from=BUILD $APP_HOME .
EXPOSE 25565
ENTRYPOINT exec java -jar spigot.jar nogui
