package com.generoso.ft.identity.state;

import lombok.Getter;
import lombok.Setter;
import org.hawkular.agent.prometheus.types.MetricFamily;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Setter
@Component
public class MetricsState {

    private List<MetricFamily> initialMetrics;

    private List<MetricFamily> newMetrics;
}
