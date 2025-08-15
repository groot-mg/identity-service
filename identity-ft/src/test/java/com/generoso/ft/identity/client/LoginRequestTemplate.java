package com.generoso.ft.identity.client;

import com.generoso.ft.identity.client.model.Endpoint;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Qualifier("serviceRequest")
public class LoginRequestTemplate extends RequestTemplate {

    public LoginRequestTemplate(@Value("${service.host}") String host,
                                @Value("${service.context-path:}") String contextPath) {
        super(host, contextPath);
    }

    @Override
    public Endpoint getEndpoint() {
        return Endpoint.AUTH_LOGIN;
    }
}
