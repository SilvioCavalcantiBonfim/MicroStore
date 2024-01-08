FROM eclipse-temurin:21-jre-alpine

RUN mkdir app
ARG JAR_FILE
# ADD /target/dist/lib /app/lib
ADD /target/dist/${JAR_FILE} /app/service.jar
WORKDIR /app
ENTRYPOINT java -jar service.jar
