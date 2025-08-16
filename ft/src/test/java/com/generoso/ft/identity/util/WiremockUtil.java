package com.generoso.ft.identity.util;

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;
import com.google.common.collect.ImmutableMap;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import static java.util.UUID.fromString;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WiremockUtil {

    private static final Map<String, UUID> FRIENDLY_DOWNSTREAM_NAME_UUIDS = ImmutableMap.<String, UUID>builder()
            .put("keycloak-auth", fromString("81d30d73-4925-4f89-80ef-0da0e375d311"))
            .put("keycloak-create-user", fromString("39e6b2dc-ab78-4762-9cc2-2ccc09ec2c9f"))
            .build();

    public void primeResponseWithBody(String downstream, ResponseDefinitionBuilder response) {
        primeResponseWithBody(getStubId(downstream), response);
    }

    public void primeResponseWithBody(UUID stubId, ResponseDefinitionBuilder response) {
        var existingStubMapping = getStubMapping(stubId);
        var method = existingStubMapping.getRequest().getMethod().toString();
        var urlMatcher = existingStubMapping.getRequest().getUrlMatcher();

        editStub(request(method, urlMatcher).withId(stubId).willReturn(response));
    }

    public ResponseDefinitionBuilder buildErrorResponse(int statusCode) {
        return aResponse().withStatus(statusCode);
    }

    private StubMapping getStubMapping(UUID stubId) {
        return WireMock.getSingleStubMapping(stubId);
    }

    private UUID getStubId(String endpoint) {
        return ofNullable(FRIENDLY_DOWNSTREAM_NAME_UUIDS.get(endpoint))
                .orElseThrow(() -> new IllegalStateException(format("No existing wiremock mapping found for endpoint [%s]", endpoint)));
    }
}
