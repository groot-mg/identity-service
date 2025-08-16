package com.generoso.identity.exception;

import lombok.Getter;

@Getter
public class RequestException extends RuntimeException {

    private final int statusCode;

    public RequestException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
}
