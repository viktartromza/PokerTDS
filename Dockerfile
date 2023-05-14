FROM openjdk:17
RUN mkdir /app
WORKDIR /app
COPY target/PokerTDS-0.0.1-SNAPSHOT.jar /app
ENTRYPOINT java -jar /app/PokerTDS-0.0.1-SNAPSHOT.jar