package com.generoso.identity.api.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
public class IncomingRequestLogFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        var httpServletRequest = (HttpServletRequest) request;
        var path = httpServletRequest.getRequestURI();
        var method = httpServletRequest.getMethod();

        log.info("Incoming request {} {}", method, path);

        chain.doFilter(request, response);
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
