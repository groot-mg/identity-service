FROM eclipse-temurin:21-jre-alpine

LABEL MAINTAINER="Mauricio Generoso"

RUN mkdir /apps

COPY build/libs/identity-service.jar /apps/identity-service.jar

# Used for docker healthcheck
RUN apk add --update \
    curl \
    && rm -rf /var/cache/apk/*

ENTRYPOINT exec java -jar /apps/identity-service.jar