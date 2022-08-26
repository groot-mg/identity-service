package com.generoso.ft.identity.util;

import com.github.tomakehurst.wiremock.client.HttpAdminClient;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.http.ResponseDefinition;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;
import com.google.common.collect.ImmutableMap;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import static java.util.UUID.fromString;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WiremockUtil {

    private static final Map<String, UUID> FRIENDLY_DOWNSTREAM_NAME_UUIDS = ImmutableMap.<String, UUID>builder()
            .put("keycloak-auth", fromString("81d30d73-4925-4f89-80ef-0da0e375d311"))
            .build();

    private final HttpAdminClient wiremockClient;

    public void primeResponseWithBody(String downstream, ResponseDefinition response) {
        primeResponseWithBody(getStubId(downstream), response);
    }

    public void primeResponseWithBody(UUID stubId, ResponseDefinition response) {
        var existingStubMapping = getStubMapping(stubId);
        existingStubMapping.setResponse(response);
        wiremockClient.editStubMapping(existingStubMapping);
    }

    public ResponseDefinitionBuilder buildErrorResponse(int statusCode) {
        return aResponse().withStatus(statusCode);
    }

    private StubMapping getStubMapping(UUID stubId) {
        return wiremockClient.getStubMapping(stubId).getItem();
    }

    private UUID getStubId(String endpoint) {
        return ofNullable(FRIENDLY_DOWNSTREAM_NAME_UUIDS.get(endpoint))
                .orElseThrow(() -> new IllegalStateException(format("No existing wiremock mapping found for endpoint [%s]", endpoint)));
    }
}
