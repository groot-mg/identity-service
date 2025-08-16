package com.generoso.identity.service;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Tags;
import io.micrometer.prometheusmetrics.PrometheusConfig;
import io.micrometer.prometheusmetrics.PrometheusCounter;
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class MetricsServiceTest {

    private final MeterRegistry meterRegistry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);

    private final MetricsService metricsService = new MetricsService(meterRegistry);

    @Test
    void thenCallingServiceToRegisterApplicationResponseTotal_shouldIncreaseMetric() {
        // Arrange
        var method = "GET";
        var path = "/example";
        var status = "200";

        // Act
        metricsService.applicationResponseTotal(method, path, status);

        // Assert
        assertThat(getCounterValue(meterRegistry, "application.responses.total",
                new String[]{"method", "path", "status"}, "GET", "/example", "200"))
                .isPresent()
                .hasValue(1.0);
    }

    private static Optional<Double> getCounterValue(MeterRegistry registry, String name, String[] labelNames, String... values) {
        var tags = getTags(labelNames, values);
        return registry.getMeters().stream().filter(
                        m -> m.getId().getName().equals(name) && m.getId().getTags()
                                .containsAll(tags.stream().toList())).findFirst()
                .map(metric -> ((PrometheusCounter) metric).count());
    }

    private static Tags getTags(String[] labelNames, String[] values) {
        var tags = Tags.empty();
        var idx = 0;
        for (var label : labelNames) {
            tags = tags.and(Tag.of(label, values[idx++]));
        }

        return Tags.of(tags);
    }
}
