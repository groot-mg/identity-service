package com.generoso.ft.identity.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.generoso.ft.identity.client.RequestTemplate;
import com.generoso.ft.identity.client.model.Endpoint;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.HttpAdminClient;
import com.github.tomakehurst.wiremock.common.ClasspathFileSource;
import com.github.tomakehurst.wiremock.common.ConsoleNotifier;
import com.github.tomakehurst.wiremock.standalone.JsonFileMappingsSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.net.http.HttpClient;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

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

    @Bean(name = "localWiremockServer", initMethod = "start", destroyMethod = "stop")
    @Profile("local")
    public WireMockServer wireMockServer(@Value("${wiremock.host}") String wiremockHost,
                                         @Value("${wiremock.port}") int port) {
        return new WireMockServer(options().bindAddress(wiremockHost)
                .port(port)
                .notifier(new ConsoleNotifier(true))
                .mappingSource(new JsonFileMappingsSource(new ClasspathFileSource("mappings")))
        );
    }

    @Bean
    public HttpAdminClient wiremockClient(@Value("${wiremock.host}") String wiremockHost,
                                          @Value("${wiremock.port}") int port) {
        return new HttpAdminClient(wiremockHost, port);
    }
}
