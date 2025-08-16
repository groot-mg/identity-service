package com.generoso.identity.config.properties;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(
        KeycloakProperties.class
)
public class AppProperties {
}
