package com.generoso.ft.identity.config;


import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

@Slf4j
@Configuration
@Profile("local")
public class LocalWiremockServer {

    private WireMockServer wireMockServer;

    @Value(value = "${wiremock.host}")
    private String host;

    @Value(value = "${wiremock.port}")
    private int port;

    @PostConstruct
    public void startUp() {
        log.info("Trying to start a wiremock instance for: {}. Port: {}", host, port);
        wireMockServer = new WireMockServer(options().bindAddress(host).port(port));
        wireMockServer.start();
        log.info("Wiremock is running");
    }

    @PreDestroy
    public void shutDown() {
        log.info("Wiremock is shutting down");
        WireMock.configureFor(port);
        WireMock.shutdownServer();
        log.info("Wiremock server is down");
    }
}
