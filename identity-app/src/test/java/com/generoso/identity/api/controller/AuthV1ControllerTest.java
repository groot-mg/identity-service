package com.generoso.identity.api.controller;

import com.generoso.identity.api.dto.LoginV1Dto;
import com.generoso.identity.service.AuthService;
import com.generoso.identity.utils.JsonUtils;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthV1ControllerTest {

    private static final String CONTROLLER_MAPPING = "/v1/auth";

    @Autowired
    private JsonUtils jsonUtils;

    @Autowired
    private CommonControllerTest commonControllerTest;

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

        when(authService.login(userDto.username(), userDto.password()))
                .thenReturn(accessTokenResponse);

        // Act
        var result = this.mockMvc.perform(post(CONTROLLER_MAPPING)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUtils.asString(userDto)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn();

        // Assert
        var responseBody = result.getResponse().getContentAsString();
        assertThat(responseBody).isEqualTo("""
                {"tokenType":"Bearer","token":"token"}""");
    }

    @MethodSource("provideInputsToTestMissingField")
    @ParameterizedTest
    void whenFieldsAreMissing_shouldReturn400Response(String jsonBody, String field, String fieldMessage) throws Exception {
        commonControllerTest.testWhenFieldsAreMissingAndShouldReturn400Response(CONTROLLER_MAPPING, jsonBody,
                field, fieldMessage);
    }

    private static Stream<Arguments> provideInputsToTestMissingField() {
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
}
