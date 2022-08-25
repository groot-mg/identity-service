package com.generoso.identity.exception;

import com.generoso.identity.model.Downstream;

import static java.lang.String.format;

public class DownstreamException extends RuntimeException {

    private static final String MESSAGE_TEMPLATE = "Downstream down: %s";

    public DownstreamException(Downstream downstream) {
        super(format(MESSAGE_TEMPLATE, downstream.name()));
    }
}
