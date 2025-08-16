package com.generoso.ft.identity.client;

import com.generoso.ft.identity.client.model.Endpoint;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PrivateRequestTemplate {

    @Component
    @Qualifier("private")
    public static class PrivateHealthRequestTemplate extends RequestTemplate {

        @Autowired
        public PrivateHealthRequestTemplate(
                @Value("${service.host}") String host,
                @Value("${service.context-path:}") String contextPath
        ) {
            super(host, contextPath);
        }

        @Override
        public Endpoint getEndpoint() {
            return Endpoint.PRIVATE_HEALTH_CHECK;
        }
    }

    @Component
    @Qualifier("private")
    public static class PrivateInfoRequestTemplate extends RequestTemplate {

        public PrivateInfoRequestTemplate(
                @Value("${service.host}") String host,
                @Value("${service.context-path:}") String contextPath
        ) {
            super(host, contextPath);
        }

        @Override
        public Endpoint getEndpoint() {
            return Endpoint.PRIVATE_INFO;
        }
    }

    @Component
    @Qualifier("private")
    public static class PrivateMetricsRequestTemplate extends RequestTemplate {

        public PrivateMetricsRequestTemplate(
                @Value("${service.host}") String host,
                @Value("${service.context-path:}") String contextPath
        ) {
            super(host, contextPath);
        }

        @Override
        public Endpoint getEndpoint() {
            return Endpoint.PRIVATE_METRICS;
        }

        @Override
        public Map<String, String> defaultHeaders() {
            return new HashMap<>();
        }
    }
}
