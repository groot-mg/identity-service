FROM amazoncorretto:17-alpine

LABEL maintainer="Mauricio Generoso"

RUN mkdir /apps

COPY build/libs/identity-service.jar /apps/identity-service.jar

RUN apk add --update \
    curl \
    && rm -rf /var/cache/apk/*

ENTRYPOINT exec java -jar /apps/identity-service.jar