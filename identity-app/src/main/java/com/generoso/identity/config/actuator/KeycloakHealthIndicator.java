package com.generoso.identity.config.actuator;

import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KeycloakHealthIndicator implements HealthIndicator {

    private final Keycloak keycloak;

    @Override
    public Health health() {
        try {
            keycloak.serverInfo().getInfo();
            return Health.up().build();
        } catch (Exception ex) {
            return Health.down().build();
        }
    }
}
