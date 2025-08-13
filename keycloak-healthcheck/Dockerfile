FROM curlimages/curl:8.15.0

LABEL MAINTAINER="Mauricio Generoso"

COPY entrypoint.sh /healthcheck/entrypoint.sh
WORKDIR /healthcheck

ENTRYPOINT ["sh", "/healthcheck/entrypoint.sh"]
