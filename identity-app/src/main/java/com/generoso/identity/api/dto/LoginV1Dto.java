package com.generoso.identity.api.dto;

import jakarta.validation.constraints.NotNull;

public record LoginV1Dto(@NotNull String username, @NotNull String password) {
}
