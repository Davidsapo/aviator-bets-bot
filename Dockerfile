FROM openjdk:17-slim

WORKDIR /app

COPY target/aviator-bets-bot-0.0.1-SNAPSHOT.jar /app
COPY src/main/resources/db/aviator.db /app/db/aviator.db
COPY src/main/resources/chrome/ /app/chrome/

CMD ["java", "-jar", "aviator-bets-bot-0.0.1-SNAPSHOT.jar"]