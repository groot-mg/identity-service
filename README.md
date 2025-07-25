# identity-service

[![CI Workflow](https://github.com/groot-mg/identity-service/actions/workflows/ci-workflow.yml/badge.svg)](https://github.com/groot-mg/identity-service/actions/workflows/ci-workflow.yml) [![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=groot-mg_identity-service&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=groot-mg_identity-service) [![License: MIT](https://img.shields.io/badge/License-MIT-green.svg)](https://github.com/groot-mg/identity-service/blob/main/LICENSE)

Keycloak is an Open Source Identity and Access Management. It is used on this project with OpenID Connect. 

## Local environment
For local environment, keycloak loads pre-configured users:

Client role:
- user: client
- password: client

Sales role:
- user: sales
- password: sales

These users and roles are used across the [groot-mg](https://github.com/groot-mg) modules to allow access in specific endpoints and provides secured API.


| IMPORTANT: Never storage plain text users and passwords in a repo, mainly in a public repo. This is here for documentation purpose in case of someone wants to run it in a local environment, and this is a portfolio project, so it will be useful to have default users to be used. |
|-------------------------------------------------------------------------------------------------------------------------------| 

## How to run

See [Wiki](https://github.com/groot-mg/identity-service/wiki/Keycloak#how-to-run).
