# identity-service

Keycloak is an Open Source Identity and Access Management. It is used on this project with OpenID Connect. 

## Local environment
For local environment, keycloak loads pre-configured users:

Admin role:
- user: admin
- password: admin

Client role:
- user: client
- password: client

Sales role:
- user: sales
- password: sales

These users and roles are used across the [shopping-api](https://github.com/shopping-api) modules to allow access in specific endpoints and provides secured API.


| IMPORTANT: Never storage plain text users and passwords in a repo, mainly in a public repo. This is here for documentation purpose in case of someone wants to run it in a local environment, and this is a portfolio project, so it will be useful to have default users to be used. |
|-------------------------------------------------------------------------------------------------------------------------------| 

## How to run

See [Wiki](https://github.com/shopping-api/identity-service/wiki/Keycloak#how-to-run).