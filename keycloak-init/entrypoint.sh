#!/bin/bash

set -euo pipefail

REAL_NAME=groot-mg
users=("client" "support")
passwords=("client-password" "support-password")
roles=("client-user" "support-user")

clients=("groot-mg-identity-app")
secrets=("yeL37FpucV9slQGp6r5OTmzEu485yETv")

function error_exit() {
  echo "Error: $1"
  exit 1
}

function admin_login() {
  /opt/keycloak/bin/kcadm.sh config credentials \
    --server $KEYCLOAK_HOST \
    --realm master \
    --user $KC_BOOTSTRAP_ADMIN_USERNAME \
    --password $KC_BOOTSTRAP_ADMIN_PASSWORD \
    || error_exit "Admin login failed."
}

function create_users() {
  for i in "${!users[@]}"; do
    username=${users[$i]}
    password=${passwords[$i]}
    role=${roles[$i]}

    echo "Creating user $username"

    /opt/keycloak/bin/kcadm.sh create users -r $REAL_NAME -s username=$username -s enabled=true \
      || error_exit "Failed to create user $username."
    /opt/keycloak/bin/kcadm.sh set-password -r $REAL_NAME --username $username --new-password $password \
      || error_exit "Failed to set password for user $username."

    userId=$(/opt/keycloak/bin/kcadm.sh get users -r $REAL_NAME -q username=$username --fields id --format csv | tail -n1 | tr -d '"') \
      || error_exit "Failed to get ID for user $username."
    roleId=$(/opt/keycloak/bin/kcadm.sh get roles -r $REAL_NAME --fields id,name | grep $role -B1 | head -1 | sed 's/.*"id" : "\(.*\)",/\1/') \
      || error_exit "Failed to get ID for role $role."

    roleId=$(echo "$roleId" | xargs)
    /opt/keycloak/bin/kcadm.sh create users/$userId/role-mappings/realm -r $REAL_NAME -b "[{\"id\":\"$roleId\",\"name\":\"$role\"}]" \
      || error_exit "Failed to assign role $role to user $username."
  done

  echo "All users created successfully."
}

function set_client_secrets() {
  for i in "${!clients[@]}"; do
    clientId=${clients[$i]}
    secretValue=${secrets[$i]}

    echo "Setting client secret for $clientId"

    clientUUID=$(/opt/keycloak/bin/kcadm.sh get clients -r $REAL_NAME -q clientId=$clientId --fields id --format csv | tail -n1 | tr -d '"') \
      || error_exit "Failed to get ID for client $clientId."

    /opt/keycloak/bin/kcadm.sh update clients/$clientUUID -r $REAL_NAME -s secret=$secretValue \
      || error_exit "Failed to set secret for client $clientId."
  done

  echo "All clients updated successfully."
}
admin_login
create_users
set_client_secrets