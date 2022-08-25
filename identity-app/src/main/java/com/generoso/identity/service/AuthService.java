package com.generoso.identity.service;

import com.generoso.identity.config.properties.KeycloakProperties;
import com.generoso.identity.exception.DownstreamException;
import com.generoso.identity.model.Downstream;
import lombok.RequiredArgsConstructor;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.admin.client.Config;
import org.keycloak.admin.client.token.TokenManager;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.ProcessingException;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthService {

    private final KeycloakProperties keycloakProperties;

    public AccessTokenResponse login(String username, String password) {
        var config = buildConfig(username, password);
        var resteasyClient = new ResteasyClientBuilder().connectionPoolSize(10).build();

        try {
            var tokenManager = new TokenManager(config, resteasyClient);
            return tokenManager.getAccessToken();
        } catch (ProcessingException ex) {
            throw new DownstreamException(Downstream.KEYCLOAK);
        } catch (NotAuthorizedException ex) {
            throw new NotAuthorizedException("Wrong username or password", ex);
        }
    }

    private Config buildConfig(String username, String password) {
        return new Config(keycloakProperties.serverUrl(), keycloakProperties.realm(), username, password,
                keycloakProperties.clientId(), keycloakProperties.clientSecret());
    }
}