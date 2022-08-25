package com.generoso.ft.identity.steps;

import com.generoso.ft.identity.client.Client;
import com.generoso.ft.identity.client.RequestTemplate;
import com.generoso.ft.identity.client.model.Endpoint;
import com.generoso.ft.identity.state.ScenarioState;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RequestStepDefinitions {

    private static final Map<String, String> bodyMap;

    private final Map<Endpoint, RequestTemplate> requestTemplates;
    private final Client client;
    private final ScenarioState scenarioState;

    @Given("an endpoint {} is prepared")
    public void thePrivateEndpointIsPrepared(Endpoint endpoint) {
        RequestTemplate requestTemplate = getRequestTemplate(endpoint);
        scenarioState.setRequestTemplate(requestTemplate);
    }

    @Given("an endpoint {} is prepared with path parameter {word}")
    public void anEndpointIsPreparedWithPathParameter(Endpoint endpoint, String pathParameter) {
        RequestTemplate requestTemplate = getRequestTemplate(endpoint);
        requestTemplate.pathParameter(pathParameter);
        scenarioState.setRequestTemplate(requestTemplate);
    }

    @And("a request body is prepared for {word}")
    public void aRequestBodyIsPreparedFor(String requestBodyKey) {
        scenarioState.getRequestTemplate().body(bodyMap.get(requestBodyKey));
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

    static {
        bodyMap = new HashMap<>();
        bodyMap.put("APP_NAME",  "{\n" +
                "  \"instance\": {\n" +
                "    \"instanceId\": \"5784ed89d3cf:gateway:8080\",\n" +
                "    \"app\": \"APP_NAME\",\n" +
                "    \"appGroupName\": null,\n" +
                "    \"ipAddr\": \"172.20.0.3\",\n" +
                "    \"sid\": \"na\",\n" +
                "    \"homePageUrl\": \"http://172.20.0.3:8080/\",\n" +
                "    \"statusPageUrl\": \"http://172.20.0.3:8080/actuator/info\",\n" +
                "    \"healthCheckUrl\": \"http://172.20.0.3:8080/actuator/health\",\n" +
                "    \"secureHealthCheckUrl\": null,\n" +
                "    \"vipAddress\": \"app_name\",\n" +
                "    \"secureVipAddress\": \"app_name\",\n" +
                "    \"countryId\": 1,\n" +
                "    \"dataCenterInfo\": {\n" +
                "      \"@class\": \"com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo\",\n" +
                "      \"name\": \"MyOwn\"\n" +
                "    },\n" +
                "    \"hostName\": \"172.20.0.3\",\n" +
                "    \"status\": \"UP\",\n" +
                "    \"overriddenStatus\": \"UNKNOWN\",\n" +
                "    \"leaseInfo\": {\n" +
                "      \"renewalIntervalInSecs\": 30,\n" +
                "      \"durationInSecs\": 90,\n" +
                "      \"registrationTimestamp\": 0,\n" +
                "      \"lastRenewalTimestamp\": 0,\n" +
                "      \"evictionTimestamp\": 0,\n" +
                "      \"serviceUpTimestamp\": 0\n" +
                "    },\n" +
                "    \"isCoordinatingDiscoveryServer\": false,\n" +
                "    \"lastUpdatedTimestamp\": 1658954363496,\n" +
                "    \"lastDirtyTimestamp\": 1658954365828,\n" +
                "    \"actionType\": null,\n" +
                "    \"asgName\": null,\n" +
                "    \"port\": {\n" +
                "      \"$\": 8080,\n" +
                "      \"@enabled\": \"true\"\n" +
                "    },\n" +
                "    \"securePort\": {\n" +
                "      \"$\": 443,\n" +
                "      \"@enabled\": \"false\"\n" +
                "    },\n" +
                "    \"metadata\": {\n" +
                "      \"management.port\": \"8080\"\n" +
                "    }\n" +
                "  }\n" +
                "}");
    }
}
