package com.generoso.ft.identity.client;

import com.generoso.ft.identity.client.model.Endpoint;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Qualifier("private")
public class PrivateInfoRequestTemplate extends RequestTemplate {

    public PrivateInfoRequestTemplate(@Value("${service.host}") String host,
                                      @Value("${service.context-path:}") String contextPath) {
        super(host, contextPath);
    }

    @Override
    public Endpoint getEndpoint() {
        return Endpoint.PRIVATE_INFO;
    }
}
