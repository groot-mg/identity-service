#!/bin/bash

echo "Waiting for Keycloak to become healthy...";
for i in $(seq 1 $RETRY_MAX_ATTEMPT); do
  if curl --fail --silent --output /dev/null $KEYCLOAK_ADMIN_HOST/health; then
    echo "Keycloak is healthy.";
    exit 0;
  fi
  echo "Keycloak not ready yet.";
  sleep $RETRY_WAIT_INTERVAL_SECONDS;
done
echo "Keycloak did not become healthy in time.";
exit 1;