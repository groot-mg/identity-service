#!/bin/bash

set -euo pipefail

REAL_NAME=groot-mg
users=("client" "support")
passwords=("client-password" "support-password")
roles=("client-user" "support-user")

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
}

admin_login
create_users

echo "All users created successfully."