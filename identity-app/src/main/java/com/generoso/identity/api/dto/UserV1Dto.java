package com.generoso.identity.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.generoso.identity.model.UserType;
import lombok.Builder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserV1Dto(
        @NotNull
        String username,
        @NotNull
        @Email String email,
        @NotNull
        String password,
        @NotNull
        String repeatPassword,
        @NotNull String firstName,
        @NotNull String lastName,
        @NotNull
        UserType userType) {
}