FROM quay.io/keycloak/keycloak:26.3.2

LABEL MAINTAINER="Mauricio Generoso"

COPY realm-export.json opt/keycloak/data/import/realm-export.json

ENTRYPOINT ["/opt/keycloak/bin/kc.sh", "start-dev", "--health-enabled=true", "--metrics-enabled=true", "--import-realm"]