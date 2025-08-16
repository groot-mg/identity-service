package com.generoso.identity.service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Tags;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MetricsService {

    private final MeterRegistry meterRegistry;

    public void applicationResponseTotal(String method, String path, String status) {
        var tags = Tags.of(
                Tag.of("method", method),
                Tag.of("path", path),
                Tag.of("status", status)
        );

        Counter.builder("application.responses.total")
                .description("Metrics for application responses per endpoint, method and http response code.")
                .tags(tags)
                .register(meterRegistry)
                .increment();
    }
}
