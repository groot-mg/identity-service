package com.generoso.ft.identity.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.generoso.ft.identity.client.RequestTemplate;
import com.generoso.ft.identity.client.model.Endpoint;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.net.http.HttpClient;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@ComponentScan("com.generoso.ft.identity.*")
public class TestConfiguration {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public HttpClient httpClient() {
        return HttpClient.newBuilder().build();
    }

    @Bean
    public Map<Endpoint, RequestTemplate> privateRequestTemplates(
            @Qualifier("private") List<RequestTemplate> templates) {
        var map = new HashMap<Endpoint, RequestTemplate>(templates.size());
        templates.forEach(t -> map.put(t.getEndpoint(), t));
        return map;
    }

    @Bean
    public Map<Endpoint, RequestTemplate> requestTemplates(
            @Qualifier("service-request") List<RequestTemplate> templates) {
        var map = new HashMap<Endpoint, RequestTemplate>(templates.size());
        templates.forEach(t -> map.put(t.getEndpoint(), t));
        return map;
    }
}
