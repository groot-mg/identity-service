package com.generoso.ft.identity.steps;

import com.generoso.ft.identity.client.Client;
import com.generoso.ft.identity.client.RequestTemplate;
import com.generoso.ft.identity.client.model.Endpoint;
import com.generoso.ft.identity.state.ScenarioState;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.http.HttpResponse;
import java.util.Map;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RequestStepDefinitions {

    private final Map<Endpoint, RequestTemplate> requestTemplates;
    private final Client client;
    private final ScenarioState scenarioState;

    @Given("an endpoint {} is prepared")
    public void thePrivateEndpointIsPrepared(Endpoint endpoint) {
        RequestTemplate requestTemplate = getRequestTemplate(endpoint);
        scenarioState.setRequestTemplate(requestTemplate);
    }

    @Given("an endpoint {} is prepared with body {word}")
    public void thePrivateEndpointIsPreparedWithBody(Endpoint endpoint, String body) {
        RequestTemplate requestTemplate = getRequestTemplate(endpoint);
        requestTemplate.body(body);
        scenarioState.setRequestTemplate(requestTemplate);
    }

    @When("the request is sent")
    public void theEndpointReceivesARequest() {
        HttpResponse<String> response = client.execute(scenarioState.getRequestTemplate());
        scenarioState.setActualResponse(response);
    }

    private RequestTemplate getRequestTemplate(Endpoint endpoint) {
        if (requestTemplates.containsKey(endpoint)) {
            return requestTemplates.get(endpoint);
        }

        throw new RuntimeException("Invalid request template provided.");
    }
}
