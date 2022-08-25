package com.generoso.ft.identity.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.generoso.ft.identity.client.RequestTemplate;
import com.generoso.ft.identity.client.model.Endpoint;
import com.generoso.ft.identity.client.model.JsonMapper;
import com.generoso.ft.identity.client.model.PrivateHealthResponse;
import com.generoso.ft.identity.state.ScenarioState;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PrivateStepDefinitions {

    private final Map<Endpoint, RequestTemplate> privateRequestTemplates;
    private final ScenarioState scenarioState;
    private final JsonMapper jsonMapper;
    private final ObjectMapper objectMapper;

    @Given("a private endpoint {} is prepared")
    public void thePrivateEndpointIsPrepared(Endpoint endpoint) {
        RequestTemplate requestTemplate = getRequestTemplate(endpoint);
        scenarioState.setRequestTemplate(requestTemplate);
    }

    @Then("the health response body of the message should have the status {string}")
    public void theHealthResponseBodyOfTheMessageShouldHaveTheStatus(String expectedResponseBody) {
        var response = scenarioState.getActualResponse();
        var responseObj = jsonMapper.fromJson(response.body(), PrivateHealthResponse.class);
        assertThat(responseObj.status()).isEqualTo(expectedResponseBody);
    }

    @Then("health components should contain the status {word}:")
    public void healthComponentsShouldContainTheStatus(String status, List<String> componentNames) throws JsonProcessingException {
        JsonNode jsonResponse = objectMapper.readTree(scenarioState.getActualResponseBody());
        JsonNode components = jsonResponse.get("components");

        componentNames.forEach(componentName -> assertThat(components.get(componentName).get("status").asText()).isEqualTo(status));
    }

    @Then("the body of the message contains {string}")
    public void theBodyOfTheMessageContains(String expectedResponseBodyContent) {
        assertTrue(scenarioState.getActualResponse().body().contains(expectedResponseBodyContent));
    }

    @Then("it should return (.*) information containing the following keys and values:$")
    public void theInfoContains(String key, Map<String, String> expectedInfo) throws JsonProcessingException {
        JsonNode jsonResponse = objectMapper.readTree(scenarioState.getActualResponseBody());
        JsonNode node = key.equals("java") ? jsonResponse.get("app") : jsonResponse;

        for (Map.Entry<String, String> entry : expectedInfo.entrySet()) {
            String actualInfo = node.get(key).get(entry.getKey()).asText();
            assertThat(actualInfo).matches(entry.getValue());
        }
    }

    @Then("the response body contains:")
    public void theResponseBodyContains(List<String> keys) throws JsonProcessingException {
        JsonNode jsonResponse = objectMapper.readTree(scenarioState.getActualResponseBody());
        keys.forEach(keyName -> assertTrue(jsonResponse.has(keyName)));
    }

    private RequestTemplate getRequestTemplate(Endpoint endpoint) {
        if (privateRequestTemplates.containsKey(endpoint)) {
            return privateRequestTemplates.get(endpoint);
        }

        throw new RuntimeException("Invalid private request template provided.");
    }
}
