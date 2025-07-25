Feature: Checking auth endpoint scenarios

  Background:
    Given initial metrics are gathered

  Scenario: When username and password are correctly provided, a success login is expected
    Given an endpoint AUTH_LOGIN is prepared with body {"username":"test-username","password":"12345"}
    When the request is sent
    Then the response status code should be 200
    And metrics are gathered again
    And the application_responses_total metric for endpoint AUTH_LOGIN with status response code 200 has incremented by 1
    And the application_responses_total metric for endpoint AUTH_LOGIN with status response code 400 has incremented by 0
    And the application_responses_total metric for endpoint AUTH_LOGIN with status response code 500 has incremented by 0

  Scenario Outline: When a field is not provided, 400 response status code is given with expected message
    Given an endpoint AUTH_LOGIN is prepared with body <BODY>
    When the request is sent
    Then the response status code should be 400
    And error validation response should contain status <STATUS> and error <ERROR> and field <FIELD> and field message <FIELD_MESSAGE>
    And metrics are gathered again
    And the application_responses_total metric for endpoint AUTH_LOGIN with status response code 200 has incremented by 0
    And the application_responses_total metric for endpoint AUTH_LOGIN with status response code 400 has incremented by 1
    And the application_responses_total metric for endpoint AUTH_LOGIN with status response code 500 has incremented by 0
    Examples:
      | BODY                         | STATUS | ERROR                  | FIELD             | FIELD_MESSAGE                                        |
      | {"password":"12345"}         | 400    | Field validation error | username          | username must not be null                            |
      | {"username":"test-username"} | 400    | Field validation error | password          | password must not be null                            |
      | {}                           | 400    | Field validation error | password,username | password must not be null,username must not be null |

  Scenario: When keycloak is down, login error response is returned as expected
    Given an endpoint AUTH_LOGIN is prepared with body {"username":"test-username","password":"12345"}
    And keycloak-auth is primed to return 500 response status code
    When the request is sent
    Then the response status code should be 500
    And error response should contain status 500 with error Error connection to a downstream and detail Downstream: KEYCLOAK
    And metrics are gathered again
    And the application_responses_total metric for endpoint AUTH_LOGIN with status response code 200 has incremented by 0
    And the application_responses_total metric for endpoint AUTH_LOGIN with status response code 400 has incremented by 0
    And the application_responses_total metric for endpoint AUTH_LOGIN with status response code 500 has incremented by 1