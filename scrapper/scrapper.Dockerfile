FROM eclipse-temurin:21

COPY target/scrapper.jar scrapper.jar

ENV JAVA_OPTS=""

ENTRYPOINT exec java $JAVA_OPTS -jar /scrapper.jar

# docker build -t scrapper_app:latest -f scrapper.Dockerfile .
# docker run --network host -it --rm scrapper_app
