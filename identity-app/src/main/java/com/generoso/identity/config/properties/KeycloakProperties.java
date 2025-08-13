package com.generoso.identity.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "keycloak")
public record KeycloakProperties(
        String serverUrl,
        String realm,
        String clientId,
        String clientSecret
) {
}
