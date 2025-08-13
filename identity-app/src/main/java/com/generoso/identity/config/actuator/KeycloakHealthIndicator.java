package com.generoso.identity.config.actuator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KeycloakHealthIndicator implements HealthIndicator {

    private final Keycloak keycloak;

    @Override
    public Health health() {
        try {
            keycloak.tokenManager().grantToken();
            return Health.up().build();
        } catch (Exception ex) {
            log.error("Keycloak healthcheck error: {}", ex.getMessage());
            return Health.down().build();
        }
    }
}
