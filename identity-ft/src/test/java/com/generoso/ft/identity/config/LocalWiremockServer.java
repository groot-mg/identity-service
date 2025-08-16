package com.generoso.ft.identity.config;

import com.github.tomakehurst.wiremock.client.HttpAdminClient;
import com.github.tomakehurst.wiremock.client.WireMock;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Slf4j
@Configuration
@Profile("local")
public class LocalWiremockServer {

    @Value(value = "${wiremock.host}")
    private String host;

    @Value(value = "${wiremock.port}")
    private int port;

    @PostConstruct
    public void startUp() {
        log.info("Trying to start a wiremock instance for: {}, port: {}", host, port);
        WireMock.configureFor(host, port);
        log.info("Wiremock is configured");
    }

    @PreDestroy
    public void shutDown() {
        log.info("Wiremock is shutting down");
        WireMock.configureFor(port);
        WireMock.shutdownServer();
        log.info("Wiremock server is down");
    }
}
