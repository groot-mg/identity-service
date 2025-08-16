package com.generoso.ft.identity.steps;

import com.generoso.ft.identity.client.MetricsClientRequest;
import com.generoso.ft.identity.client.model.Endpoint;
import com.generoso.ft.identity.state.MetricsState;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import lombok.RequiredArgsConstructor;
import org.hawkular.agent.prometheus.types.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MetricsStepDefinitions {

    private final MetricsClientRequest metricsClientRequest;
    private final MetricsState metricsState;

    @Given("initial metrics are gathered")
    public void gatherInitialMetrics() {
        var metricFamilies = metricsClientRequest.collectMetrics();
        metricsState.setInitialMetrics(metricFamilies);
    }

    @Then("metrics are gathered again")
    public void gatherMetricsAgain() {
        var metricFamilies = metricsClientRequest.collectMetrics();
        metricsState.setNewMetrics(metricFamilies);
    }

    @Then("the {word} metric for endpoint {} with status response code {word} has incremented by {int}")
    public void theMetricForEndpointWithStatusResponseHasIncrementedBy(String metricName, Endpoint endpoint, String statusCode, int increment) {
        var initialCount = getResourceAndStatusCounterValue(metricsState.getInitialMetrics(), metricName, endpoint, statusCode);
        var newCount = getResourceAndStatusCounterValue(metricsState.getNewMetrics(), metricName, endpoint, statusCode);
        assertThat(newCount).isEqualTo(initialCount + increment);
    }

    @Then("the {word} metric for endpoint {} with parameter {word} and status response code {word} has incremented by {int}")
    public void theMetricForEndpointWithParameterAndStatusResponseHasIncrementedBy(String metricName,
                                                                                   Endpoint endpoint,
                                                                                   String pathParameter,
                                                                                   String statusCode,
                                                                                   int increment) {
        var path = String.format("%s/%s", endpoint.getPath(), pathParameter);
        var initialCount = getResourceAndStatusCounterValue(metricsState.getInitialMetrics(), metricName,
                endpoint.getMethod(), path, statusCode);
        var newCount = getResourceAndStatusCounterValue(metricsState.getNewMetrics(), metricName,
                endpoint.getMethod(), path, statusCode);
        assertThat(newCount).isEqualTo(initialCount + increment);
    }

    private double getResourceAndStatusCounterValue(List<MetricFamily> metrics, String metricName, Endpoint endpoint, String status) {
        return getResourceAndStatusCounterValue(metrics, metricName, endpoint.getMethod(), endpoint.getPath(), status);
    }

    private double getResourceAndStatusCounterValue(List<MetricFamily> metrics, String metricName, String method,
                                                    String path, String status) {
        return sumCounterMetric(metrics, metricName, List.of(
                metric -> metric.getLabels().get("method").equals(method),
                metric -> metric.getLabels().get("path").equals(path),
                metric -> metric.getLabels().get("status").equals(status)));
    }

    private double sumCounterMetric(List<MetricFamily> metricFamilies, String metricName, List<Predicate<Metric>> filters) {
        return sumMetric(metricFamilies, metricName, MetricType.COUNTER, filters);
    }

    private double sumMetric(List<MetricFamily> metricFamilies, String metricName, MetricType metricType, List<Predicate<Metric>> filters) {
        var metricsStream = getMetricsStreamWithNameAndType(metricFamilies, metricName, metricType);
        return applyFilters(metricsStream, filters)
                .map(MetricWrapper::new)
                .mapToDouble(MetricWrapper::getValue)
                .sum();
    }

    private Stream<Metric> getMetricsStreamWithNameAndType(List<MetricFamily> metricFamilies, String metricName, MetricType metricType) {
        return metricFamilies.stream()
                .filter(mf -> mf.getName().equals(metricName) && mf.getType().equals(metricType))
                .flatMap(mf -> mf.getMetrics().stream());
    }

    private Stream<Metric> applyFilters(Stream<Metric> metricsStream, List<Predicate<Metric>> filters) {
        for (var filter : filters) {
            metricsStream = metricsStream.filter(filter);
        }

        return metricsStream;
    }

    private record MetricWrapper(Metric metric) {

        private double getValue() {
            if (metric instanceof Counter counter) {
                return counter.getValue();
            } else if (metric instanceof Gauge gauge) {
                return gauge.getValue();
            } else if (metric instanceof Histogram histogram) {
                return histogram.getSampleCount();
            }

            throw new UnsupportedOperationException("Invalid metric type");
        }
    }
}
