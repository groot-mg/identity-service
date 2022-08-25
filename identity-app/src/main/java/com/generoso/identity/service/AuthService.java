package com.generoso.identity.service;

import com.generoso.identity.config.properties.KeycloakProperties;
import lombok.RequiredArgsConstructor;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.admin.client.Config;
import org.keycloak.admin.client.token.TokenManager;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthService {

    private final KeycloakProperties keycloakProperties;

    public AccessTokenResponse login(String username, String password) {
        var config = buildConfig(username, password);
        var resteasyClient = new ResteasyClientBuilder().connectionPoolSize(10).build();
        var tokenManager = new TokenManager(config, resteasyClient);
        return tokenManager.getAccessToken();
    }

    private Config buildConfig(String username, String password) {
        return new Config(keycloakProperties.serverUrl(), keycloakProperties.realm(), username, password,
                keycloakProperties.clientId(), keycloakProperties.clientSecret());
    }
}