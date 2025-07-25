package com.generoso.identity.api.controller;

import com.generoso.identity.api.dto.LoginV1Dto;
import com.generoso.identity.exception.DownstreamException;
import com.generoso.identity.exception.error.ValidationErrorDetails;
import com.generoso.identity.model.Downstream;
import com.generoso.identity.service.AuthService;
import jakarta.ws.rs.NotAuthorizedException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.stream.Stream;

import static com.generoso.identity.api.controller.CommonControllerTest.missingFieldsAssert400Response;
import static com.generoso.identity.utils.JsonUtils.asJsonString;
import static com.generoso.identity.utils.JsonUtils.fromJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthV1ControllerTest {

    private static final String CONTROLLER_MAPPING = "/v1/auth";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Test
    void whenLoginSuccessfully_shouldReturnTokenAndTokenType() throws Exception {
        // Arrange
        var userDto = new LoginV1Dto("username", "password");
        var accessTokenResponse = new AccessTokenResponse();
        accessTokenResponse.setTokenType("Bearer");
        accessTokenResponse.setToken("token");

        when(authService.login(userDto.username(), userDto.password())).thenReturn(accessTokenResponse);

        // Act
        var result = this.mockMvc.perform(post(CONTROLLER_MAPPING)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userDto)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn();

        // Assert
        var responseBody = result.getResponse().getContentAsString();
        assertThat(responseBody).isEqualTo("""
                {"tokenType":"Bearer","token":"token"}""");
    }

    @MethodSource
    @ParameterizedTest
    void whenFieldsAreMissing_shouldReturn400Response(String jsonBody, String field, String fieldMessage)
            throws Exception {
        missingFieldsAssert400Response(mockMvc, CONTROLLER_MAPPING, jsonBody, field, fieldMessage);
    }

    private static Stream<Arguments> whenFieldsAreMissing_shouldReturn400Response() {
        return Stream.of(
                Arguments.of("""
                                {"password":"password"}""",
                        "username",
                        "username must not be null"
                ),
                Arguments.of("""
                                {"username":"username"}""",
                        "password",
                        "password must not be null"
                ),
                Arguments.of("""
                                {}""",
                        "password,username",
                        "password must not be null,username must not be null"
                )
        );
    }

    @Test
    void whenLoginIsWrong_shouldReturn401Unauthorized() throws Exception {
        // Arrange
        var userDto = new LoginV1Dto("username", "password");
        when(authService.login(userDto.username(), userDto.password())).thenThrow(
                new NotAuthorizedException("Unauthorized message", new NotAuthorizedException("internal exception"))
        );

        // Act
        var result = this.mockMvc.perform(post(CONTROLLER_MAPPING)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userDto)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isUnauthorized())
                .andReturn();

        // Assert
        var responseBody = result.getResponse().getContentAsString();
        var errorDetails = fromJson(responseBody, ValidationErrorDetails.class);

        assertThat(errorDetails.getStatus()).isEqualTo(401);
        assertThat(errorDetails.getError()).isEqualTo("Unauthorized");
        assertThat(errorDetails.getDetail()).isEqualTo("Unauthorized message");
        assertThat(errorDetails.getTimestamp()).isNotNull();
    }

    @Test
    void whenApplicationIsUnableToReachKeycloak_shouldReturn500() throws Exception {
        // Arrange
        var userDto = new LoginV1Dto("username", "password");
        when(authService.login(userDto.username(), userDto.password())).thenThrow(
                new DownstreamException(Downstream.KEYCLOAK));

        // Act
        var result = this.mockMvc.perform(post(CONTROLLER_MAPPING)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userDto)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isInternalServerError())
                .andReturn();

        // Assert
        var responseBody = result.getResponse().getContentAsString();
        var errorDetails = fromJson(responseBody, ValidationErrorDetails.class);

        assertThat(errorDetails.getStatus()).isEqualTo(500);
        assertThat(errorDetails.getError()).isEqualTo("Error connection to a downstream");
        assertThat(errorDetails.getDetail()).isEqualTo("Downstream: KEYCLOAK");
        assertThat(errorDetails.getTimestamp()).isNotNull();
    }
}
