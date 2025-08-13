# identity-service

[![CI Workflow](https://github.com/groot-mg/identity-service/actions/workflows/ci-workflow.yml/badge.svg)](https://github.com/groot-mg/identity-service/actions/workflows/ci-workflow.yml) [![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=groot-mg_identity-service&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=groot-mg_identity-service) [![License: MIT](https://img.shields.io/badge/License-MIT-green.svg)](https://github.com/groot-mg/identity-service/blob/main/LICENSE)

Keycloak is an Open Source Identity and Access Management. It is used on this project with OpenID Connect.

The `keycloak` and the `identity-service` on this repository are just a small part of the [groot-mg](https://github.com/groot-mg) project.
See the full [documentation](https://github.com/groot-mg/docs) to understand how all the services run together and how they interact and what functionalities are avaialble. 

## Local environment

This repository sets up `keycloak` for local environment, it pre-loads configuration
from [realm-export.json](./keycloak/config/realm-export.json) and it also creates default users
via [keycloak-init](./keycloak-init).

### Default Realm

The default realm loaded via json file contains a realm named `groot-mg`.

### Default Client

Some clients are loaded via config as well:

- `groot-mg-web-app`: to be used on frontend with `PKCE` for user authentication
- `groot-mg-identity-service`: the `identity-service` has an endpoint to create users and an endpoint to log in with
  `username` and `password`, it is only for study proposal.
- `groot-mg-backend`: Used on the resource-servers to validate user's tokens and user's roles to allow or block access
  to certain resources/endpoints.

### Default roles

There are two roles to control the access to the endpoints:

- `client-user`: has a client view of the functionalities.
- `support-user`: has an IT support view of the functionalities.

### Default users

The [keycloak-init](./keycloak-init) has a script that creates the following users:

| username | password         | role         |
|----------|------------------|--------------|
| client   | client-password  | client-user  |
| support  | support-password | support-user |

| IMPORTANT: Never storage plain text users and passwords in a repo, mainly in a public repo. This is here for documentation purpose in case of someone wants to run it in a local environment, and this is a portfolio project, so it will be useful to have default users to be used. |
|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------| 

## How to run

See [Wiki](https://github.com/groot-mg/identity-service/wiki/Keycloak#how-to-run).
