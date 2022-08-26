package com.generoso.identity.api.filter;

import com.generoso.identity.config.MetricsConfig;
import io.prometheus.client.Counter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.servlet.ServletException;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {MetricsConfig.class})
class ApplicationResponsesMetricsFilterTest {

    @Autowired
    private Counter responseCounter;

    @Test
    void shouldIncreaseTheRequestMetricCounter() throws ServletException, IOException {
        // Arrange
        var filter = new ApplicationResponsesMetricsFilter(responseCounter);

        var request = new MockHttpServletRequest();
        request.setMethod("GET");
        request.setRequestURI("/unit-test");
        var response = new MockHttpServletResponse();
        response.setStatus(200);
        var filterChain = new MockFilterChain();

        // Act
        filter.doFilter(request, response, filterChain);

        // Assert
        assertThat(responseCounter.labels("GET", "/unit-test", "200").get()).isEqualTo(1);
    }
}
