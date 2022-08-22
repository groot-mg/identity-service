package com.generoso.identity.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserV1Dto(String username, String emailId, String password, String firstName, String lastName) {
}