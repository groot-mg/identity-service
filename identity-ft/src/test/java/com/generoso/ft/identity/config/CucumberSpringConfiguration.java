package com.generoso.ft.identity.config;

import com.generoso.ft.identity.YamlFileApplicationContextInitializer;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {
        TestConfiguration.class,
        LocalIdentityService.class
},
        initializers = YamlFileApplicationContextInitializer.class)
@CucumberContextConfiguration
public class CucumberSpringConfiguration {
}