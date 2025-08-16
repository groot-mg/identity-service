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
    public static class HealthRequestTemplate extends RequestTemplate {

        @Autowired
        public HealthRequestTemplate(
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
    public static class InfoRequestTemplate extends RequestTemplate {

        public InfoRequestTemplate(
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
    public static class MetricsRequestTemplate extends RequestTemplate {

        public MetricsRequestTemplate(
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

    @Component
    @Qualifier("private")
    public static class OpenApiDocsRequestTemplate extends RequestTemplate {

        public OpenApiDocsRequestTemplate(
                @Value("${service.host}") String host,
                @Value("${service.context-path:}") String contextPath
        ) {
            super(host, contextPath);
        }

        @Override
        public Endpoint getEndpoint() {
            return Endpoint.OPEN_API_DOCS;
        }
    }

    @Component
    @Qualifier("private")
    public static class OpenApiUIRequestTemplate extends RequestTemplate {

        public OpenApiUIRequestTemplate(
                @Value("${service.host}") String host,
                @Value("${service.context-path:}") String contextPath
        ) {
            super(host, contextPath);
        }

        @Override
        public Endpoint getEndpoint() {
            return Endpoint.OPEN_API_UI;
        }
    }

    @Component
    @Qualifier("private")
    public static class OpenApiSwaggerHtmlIndexRequestTemplate extends RequestTemplate {

        public OpenApiSwaggerHtmlIndexRequestTemplate(
                @Value("${service.host}") String host,
                @Value("${service.context-path:}") String contextPath
        ) {
            super(host, contextPath);
        }

        @Override
        public Endpoint getEndpoint() {
            return Endpoint.OPEN_API_SWAGGER_HTML_INDEX;
        }
    }
}
