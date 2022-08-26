package com.generoso.identity.config;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Counter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricsConfig {

    @Bean
    public CollectorRegistry collectorRegistry() {
        return new CollectorRegistry();
    }

    @Bean
    public Counter responseCounter(CollectorRegistry collectorRegistry) {
        return Counter.build()
                .name("application_responses_total")
                .help("Metrics for application responses per endpoint, method and http response code.")
                .labelNames("method", "path", "status")
                .register(collectorRegistry);
    }
}
