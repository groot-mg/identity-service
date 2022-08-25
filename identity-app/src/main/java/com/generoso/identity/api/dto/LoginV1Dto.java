package com.generoso.identity.api.dto;

import javax.validation.constraints.NotNull;

public record LoginV1Dto(
        @NotNull(message = "should be provided")
        String username,
        @NotNull(message = "should be provided")
        String password) {
}
