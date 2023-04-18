package com.generoso.ft.identity.config;

import com.generoso.identity.IdentityApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

@Profile("local")
@Configuration
@DependsOn("localWiremockServer")
public class LocalIdentityService {

    private ConfigurableApplicationContext sdApplicationContext;

    @PostConstruct
    public void startUp() {
        sdApplicationContext = SpringApplication.run(IdentityApplication.class);
    }

    @PreDestroy
    public void shutDown() {
        sdApplicationContext.close();
    }
}