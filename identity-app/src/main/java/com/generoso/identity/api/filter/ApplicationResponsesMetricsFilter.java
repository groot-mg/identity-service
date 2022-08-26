package com.generoso.identity.api.filter;

import io.prometheus.client.Counter;
import lombok.RequiredArgsConstructor;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class ApplicationResponsesMetricsFilter implements Filter {

    private final Counter responseCounter;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        chain.doFilter(request, response);

        var httpServletRequest = (HttpServletRequest) request;
        var path = httpServletRequest.getRequestURI();
        var method = httpServletRequest.getMethod();
        var statusCode = String.valueOf(((HttpServletResponse) response).getStatus());
        responseCounter.labels(method, path, statusCode).inc();
    }

    @Override
    public void init(FilterConfig filterConfig) {
        // nothing to do
    }

    @Override
    public void destroy() {
        // nothing to do
    }
}
