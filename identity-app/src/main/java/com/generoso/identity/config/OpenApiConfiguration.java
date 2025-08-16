package com.generoso.identity.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * For local environment: <a href="http://localhost:8181/identity/swagger">here</a>
 */
@Configuration
public class OpenApiConfiguration {

    private final BuildProperties buildProperties;

    @Autowired
    public OpenApiConfiguration(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().info(apiInfo());
    }

    private Info apiInfo() {
        return new Info()
                .title("Identity Service")
                .description("Spring Boot Identity Service")
                .version(buildProperties.getVersion())
                .contact(apiContact())
                .license(apiLicence());
    }

    private License apiLicence() {
        return new License()
                .name("MIT Licence")
                .url("https://opensource.org/licenses/mit-license.php");
    }

    private Contact apiContact() {
        return new Contact()
                .name("Mauricio Generoso")
                .email("mauricio.marquesgeneroso@gmail.com")
                .url("https://github.com/mauriciogeneroso");
    }
}
