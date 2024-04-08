FROM eclipse-temurin:21

COPY target/bot.jar bot.jar

ENV JAVA_OPTS=""

ENTRYPOINT exec java $JAVA_OPTS -jar /bot.jar

# docker build -t bot_app:latest -f bot.Dockerfile .
# docker run --network host -e TELEGRAM_TOKEN=${TELEGRAM_TOKEN} -it --rm bot_app
