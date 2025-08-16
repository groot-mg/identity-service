package com.generoso.ft.identity.client;

import com.generoso.ft.identity.client.model.Endpoint;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IdentityServiceRequestTemplate {

    @Component
    @Qualifier("serviceRequest")
    public static class LoginRequestTemplate extends RequestTemplate {

        public LoginRequestTemplate(
                @Value("${service.host}") String host,
                @Value("${service.context-path:}") String contextPath
        ) {
            super(host, contextPath);
        }

        @Override
        public Endpoint getEndpoint() {
            return Endpoint.AUTH_LOGIN;
        }
    }

    @Component
    @Qualifier("serviceRequest")
    public static class CreateUserRequestTemplate extends RequestTemplate {

        public CreateUserRequestTemplate(
                @Value("${service.host}") String host,
                @Value("${service.context-path:}") String contextPath
        ) {
            super(host, contextPath);
        }

        @Override
        public Endpoint getEndpoint() {
            return Endpoint.CREATE_USER;
        }
    }

}
