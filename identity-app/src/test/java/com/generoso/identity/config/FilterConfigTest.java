package com.generoso.identity.config;

import com.generoso.identity.api.filter.ApplicationResponsesMetricsFilter;
import com.generoso.identity.api.filter.IncomingRequestLogFilter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {MetricsConfig.class, FiltersConfig.class})
class FilterConfigTest {

    @Autowired
    private FilterRegistrationBean<IncomingRequestLogFilter> incomingRequestLogFilter;

    @Autowired
    private FilterRegistrationBean<ApplicationResponsesMetricsFilter> applicationResponsesMetricsFilter;

    @Test
    void shouldCreateFiltersInCorrectOrder() {
        assertThat(incomingRequestLogFilter.getOrder()).isZero();
        assertThat(applicationResponsesMetricsFilter.getOrder()).isEqualTo(1);
    }
}
