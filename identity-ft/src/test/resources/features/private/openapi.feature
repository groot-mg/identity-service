Feature: Checking Open API endpoints exist

  Scenario: Open API docs endpoint is available in the application
    Given a private endpoint OPEN_API_DOCS is prepared
    When the request is sent
    Then the response status code should be 200

  Scenario: Open API UI endpoint is available in the application
    Given a private endpoint OPEN_API_UI is prepared
    When the request is sent
    Then the response status code should be 302
     And the response header location contains the value /identity/private/swagger-ui/index.html

  Scenario: Swagger HTML is available in the application
    Given a private endpoint OPEN_API_SWAGGER_HTML_INDEX is prepared
    When the request is sent
    Then the response status code should be 200
