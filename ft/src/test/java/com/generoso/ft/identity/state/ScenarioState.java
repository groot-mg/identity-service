package com.generoso.ft.identity.state;

import com.generoso.ft.identity.client.RequestTemplate;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.net.http.HttpResponse;

@Setter
@Component
public class ScenarioState {

    private RequestTemplate requestTemplate;
    private HttpResponse<String> actualResponse;

    public RequestTemplate getRequestTemplate() {
        if (requestTemplate == null) {
            throw new RuntimeException("The request template wasn't provided.");
        }

        return requestTemplate;
    }

    public HttpResponse<String> getActualResponse() {
        if (actualResponse == null) {
            throw new RuntimeException("The actual response wasn't provided.");
        }

        return actualResponse;
    }

    public String getActualResponseBody() {
        return getActualResponse().body();
    }
}
