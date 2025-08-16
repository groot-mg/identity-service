Feature: Checking Identity app actuator endpoints return expected outputs

  Background:
    Given initial metrics are gathered

  Scenario: When application is healthy, return 200 response status code and "UP" response body on health endpoint
    Given a private endpoint PRIVATE_HEALTH_CHECK is prepared
    When the request is sent
    Then the response status code should be 200
    And the health response body of the message should have the status "UP"
    And health components should contain the status UP:
      | keycloak |
    And metrics are gathered again
    And the application_responses_total metric for endpoint PRIVATE_HEALTH_CHECK with status response code 200 has incremented by 1

  Scenario: When application is running, display metric content
    Given a private endpoint PRIVATE_METRICS is prepared
    When the request is sent
    Then the response status code should be 200
    And the body of the message contains "jvm_buffer_count_buffers"
    And metrics are gathered again
    # one request to get metrics the first time and one request when running the test
    And the application_responses_total metric for endpoint PRIVATE_METRICS with status response code 200 has incremented by 2

  Scenario: Return correct app information when calling private/info
    Given a private endpoint PRIVATE_INFO is prepared
    When the request is sent
    Then the response status code should be 200
    And it should return build information containing the following keys and values:
      | artifact | app              |
      | name     | identity-service |
      | group    | com.generoso     |
    And the response body contains:
      | git   |
      | build |
      | java  |
    And metrics are gathered again
    And the application_responses_total metric for endpoint PRIVATE_INFO with status response code 200 has incremented by 1