package com.generoso.identity.api.filter;

import ch.qos.logback.classic.Level;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.ServletException;
import java.io.IOException;

import static com.generoso.identity.utils.LogUtils.assertMessageWasInLogs;
import static com.generoso.identity.utils.LogUtils.getListAppenderForClass;

class IncomingRequestLogFilterTest {


    @Test
    void shouldIncludeALogLine() throws ServletException, IOException {
        // Arrange
        var listAppender = getListAppenderForClass(IncomingRequestLogFilter.class);

        var filter = new IncomingRequestLogFilter();

        var request = new MockHttpServletRequest();
        request.setMethod("GET");
        request.setRequestURI("/unit-test");

        // Act
        filter.doFilter(request, new MockHttpServletResponse(), new MockFilterChain());

        // Assert
        assertMessageWasInLogs(listAppender, "Incoming request GET /unit-test", Level.INFO);
    }
}
