package com.generoso.identity.config.actuator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.token.TokenManager;
import org.springframework.boot.actuate.health.Health;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class KeycloakHealthIndicatorTest {

    private final Keycloak keycloak = mock(Keycloak.class);
    private final TokenManager tokenManager = mock(TokenManager.class);
    private final KeycloakHealthIndicator healthIndicator = new KeycloakHealthIndicator(keycloak);

    @BeforeEach
    void setUp() {
        when(keycloak.tokenManager()).thenReturn(tokenManager);
    }

    @Test
    void healthUpWhenGrantTokenSucceeds() {
        when(tokenManager.grantToken()).thenReturn(null);
        Health health = healthIndicator.health();

        verify(tokenManager).grantToken();
        assertEquals(Health.up().build().getStatus(), health.getStatus());
    }

    @Test
    void healthDownWhenGrantTokenThrowsException() {
        when(tokenManager.grantToken()).thenThrow(new RuntimeException("Keycloak unreachable"));

        Health health = healthIndicator.health();

        verify(tokenManager).grantToken();
        assertEquals(Health.down().build().getStatus(), health.getStatus());
    }
}
