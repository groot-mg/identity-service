package com.generoso.ft.identity.client;

import org.hawkular.agent.prometheus.text.TextPrometheusMetricsProcessor;
import org.hawkular.agent.prometheus.types.MetricFamily;
import org.hawkular.agent.prometheus.walkers.CollectorPrometheusMetricsWalker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class MetricsClientRequest {

    @Autowired
    private PrivateMetricsRequestTemplate privateMetricsRequestTemplate;

    @Autowired
    private Client client;

    public List<MetricFamily> collectMetrics() {
        try {
            var response = client.execute(privateMetricsRequestTemplate);
            return parsePrometheusMetrics(response.body());
        } catch (IOException e) {
            throw new RuntimeException("Unable to gather metrics", e);
        }
    }

    private List<MetricFamily> parsePrometheusMetrics(String metricEndpointResponseBody) throws IOException {
        var inputStream = new ByteArrayInputStream(metricEndpointResponseBody.getBytes(StandardCharsets.UTF_8));

        var collector = new CollectorPrometheusMetricsWalker();
        var processor = new TextPrometheusMetricsProcessor(inputStream, collector);
        processor.walk();

        return collector.getAllMetricFamilies();
    }
}
