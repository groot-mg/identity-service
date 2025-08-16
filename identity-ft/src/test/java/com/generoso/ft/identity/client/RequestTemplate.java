package com.generoso.ft.identity.client;

import com.generoso.ft.identity.client.model.Endpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;
import java.util.HashMap;
import java.util.Map;

import static java.lang.String.format;

@Slf4j
@Component
public abstract class RequestTemplate {

    private final String host;
    private final String contextPath;
    private String pathParameter;
    private Map<String, String> headers;
    private String body;

    protected RequestTemplate(String host, String contextPath) {
        this.host = host;
        this.contextPath = contextPath;
        initDefaults();
    }

    public abstract Endpoint getEndpoint();

    public HttpRequest newHttpRequest() {
        var builder = HttpRequest.newBuilder();
        builder.uri(buildUri());
        builder.method(getEndpoint().getMethod(), bodyPublisher());
        headers.forEach(builder::header);
        return builder.build();
    }

    public RequestTemplate withHeader(String key, String value) {
        this.headers.put(key, value);
        return this;
    }

    public RequestTemplate body(String body) {
        this.body = body;
        return this;
    }

    public RequestTemplate pathParameter(String pathParameter) {
        this.pathParameter = format("/%s", pathParameter);
        return this;
    }

    private void initDefaults() {
        this.pathParameter = "";
        this.headers = defaultHeaders();
    }

    public Map<String, String> defaultHeaders() {
        Map<String, String> headersMap = new HashMap<>();
        headersMap.put("Accept", "application/json");
        headersMap.put("Content-Type", "application/json");

        return headersMap;
    }

    private URI buildUri() {
        try {
            String finalUri = host + contextPath + getEndpoint().getPath() + pathParameter;
            log.info("Building Uri: {}", finalUri);
            return new URI(finalUri);
        } catch (URISyntaxException e) {
            throw new RuntimeException(format("Error creating uri: %s%s%s. Error message: %s", host, contextPath,
                    getEndpoint().getPath(), e.getMessage()));
        }
    }

    private HttpRequest.BodyPublisher bodyPublisher() {
        return body == null ? HttpRequest.BodyPublishers.noBody() : HttpRequest.BodyPublishers.ofString(body);
    }
}
