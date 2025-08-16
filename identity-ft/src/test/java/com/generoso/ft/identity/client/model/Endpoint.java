package com.generoso.ft.identity.client.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public enum Endpoint {

    PRIVATE_INFO("/identity/private/info", "GET"),
    PRIVATE_HEALTH_CHECK("/identity/private/health", "GET"),
    PRIVATE_METRICS("/identity/private/metrics", "GET"),
    AUTH_LOGIN("/identity/v1/auth", "POST"),
    CREATE_USER("/identity/v1/users", "POST");

    private final String path;
    private final String method;
}