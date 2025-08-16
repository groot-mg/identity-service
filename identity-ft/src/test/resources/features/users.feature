Feature: Checking users endpoint scenarios

  Background:
    Given initial metrics are gathered

  Scenario: Go through the happy path scenario to register a new user
    Given an endpoint CREATE_USER is prepared with body {"username":"username","email":"email@email.com","password":"password","repeatPassword":"password","firstName":"firstName","lastName":"lastName","userType":"CLIENT"}
    When the request is sent
    Then the response status code should be 201
    And metrics are gathered again
    And the application_responses_total metric for endpoint CREATE_USER with status response code 201 has incremented by 1
    And the application_responses_total metric for endpoint CREATE_USER with status response code 400 has incremented by 0
    And the application_responses_total metric for endpoint CREATE_USER with status response code 500 has incremented by 0

  Scenario Outline: When request body is not provided correctly, 400 response status code is given with expected message
    Given an endpoint CREATE_USER is prepared with body <BODY>
    When the request is sent
    Then the response status code should be 400
    And error validation response should contain status <STATUS> and error <ERROR> and field <FIELD> and field message <FIELD_MESSAGE>
    And metrics are gathered again
    And the application_responses_total metric for endpoint CREATE_USER with status response code 201 has incremented by 0
    And the application_responses_total metric for endpoint CREATE_USER with status response code 400 has incremented by 1
    And the application_responses_total metric for endpoint CREATE_USER with status response code 500 has incremented by 0
    Examples:
      | STATUS | ERROR                  | FIELD          | FIELD_MESSAGE                             | BODY                                                                                                                                                        |
      | 400    | Field validation error | username       | username must not be null                 | {"email":"email@email.com","password":"password","repeatPassword":"password","firstName":"firstName","lastName":"lastName","userType":"CLIENT"}             |
      | 400    | Field validation error | password       | password must not be null                 | {"username":"username","email":"email@email.com","repeatPassword":"password","firstName":"firstName","lastName":"lastName","userType":"CLIENT"}             |
      | 400    | Field validation error | email          | email must not be null                    | {"username":"username","password":"password","repeatPassword":"password","firstName":"firstName","lastName":"lastName","userType":"CLIENT"}                 |
      | 400    | Field validation error | email          | email must be a well-formed email address | {"username":"username","email":"email","password":"password","repeatPassword":"password","firstName":"firstName","lastName":"lastName","userType":"CLIENT"} |
      | 400    | Field validation error | repeatPassword | repeatPassword must not be null           | {"username":"username","email":"email@email.com","password":"password","firstName":"firstName","lastName":"lastName","userType":"CLIENT"}                   |
      | 400    | Field validation error | firstName      | firstName must not be null                | {"username":"username","email":"email@email.com","password":"password","repeatPassword":"password","lastName":"lastName","userType":"CLIENT"}               |
      | 400    | Field validation error | lastName       | lastName must not be null                 | {"username":"username","email":"email@email.com","password":"password","repeatPassword":"password","firstName":"firstName","userType":"CLIENT"}             |
      | 400    | Field validation error | userType       | userType must not be null                 | {"username":"username","email":"email@email.com","password":"password","repeatPassword":"password","firstName":"firstName","lastName":"lastName"}           |

  Scenario: When user is duplicated, 409 response status code is given with expected message
    Given an endpoint CREATE_USER is prepared with body {"username":"username","email":"email@email.com","password":"password","repeatPassword":"password","firstName":"firstName","lastName":"lastName","userType":"CLIENT"}
    And keycloak-create-user is primed to return 409 response status code
    When the request is sent
    Then the response status code should be 409
    And error response should contain status 409 with error Downstream request exception and detail Duplicated user
    And metrics are gathered again
    And the application_responses_total metric for endpoint CREATE_USER with status response code 200 has incremented by 0
    And the application_responses_total metric for endpoint CREATE_USER with status response code 400 has incremented by 0
    And the application_responses_total metric for endpoint CREATE_USER with status response code 409 has incremented by 1
    And the application_responses_total metric for endpoint CREATE_USER with status response code 500 has incremented by 0

  Scenario: When keycloak is down, downstream error is returned as expected
    Given an endpoint CREATE_USER is prepared with body {"username":"username","email":"email@email.com","password":"password","repeatPassword":"password","firstName":"firstName","lastName":"lastName","userType":"CLIENT"}
    And keycloak-create-user is primed to return 500 response status code
    When the request is sent
    Then the response status code should be 500
    And error response should contain status 500 with error Error connection to a downstream and detail Downstream: KEYCLOAK
    And metrics are gathered again
    And the application_responses_total metric for endpoint CREATE_USER with status response code 200 has incremented by 0
    And the application_responses_total metric for endpoint CREATE_USER with status response code 400 has incremented by 0
    And the application_responses_total metric for endpoint CREATE_USER with status response code 500 has incremented by 1