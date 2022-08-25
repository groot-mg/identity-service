package com.generoso.identity.api.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.generoso.identity.api.dto.LoginV1Dto;
import com.generoso.identity.exception.error.ValidationErrorDetails;
import com.generoso.identity.service.AuthService;
import org.junit.jupiter.api.Test;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthV1ControllerTest {

    private static final String CONTROLLER_MAPPING = "/v1/auth";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

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
                        .content(objectMapper.writeValueAsString(userDto)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn();

        // Assert
        var responseBody = result.getResponse().getContentAsString();
        assertThat(responseBody).isEqualTo("""
                {"tokenType":"Bearer","token":"token"}""");
    }

    @Test
    void whenUsernameIsMissing_shouldReturn400Response() throws Exception {
        // Act
        var result = this.mockMvc.perform(post(CONTROLLER_MAPPING)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"password\":\"password\"}"))
                .andExpect(status().isBadRequest())
                .andReturn();


        // Assert
        var responseBody = result.getResponse().getContentAsString();
        var errorDetails = objectMapper.readValue(responseBody, ValidationErrorDetails.class);
        assertThat(errorDetails.getStatus()).isEqualTo(400);
        assertThat(errorDetails.getError()).isEqualTo("Field validation error");
        assertThat(errorDetails.getField()).isEqualTo("username");
        assertThat(errorDetails.getFieldMessage()).isEqualTo("username should be provided");
    }

    @Test
    void whenPasswordIsMissing_shouldReturn400Response() throws Exception {
        // Act
        var result = this.mockMvc.perform(post(CONTROLLER_MAPPING)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"username\"}"))
                .andExpect(status().isBadRequest())
                .andReturn();


        // Assert
        var responseBody = result.getResponse().getContentAsString();
        var errorDetails = objectMapper.readValue(responseBody, ValidationErrorDetails.class);
        assertThat(errorDetails.getStatus()).isEqualTo(400);
        assertThat(errorDetails.getError()).isEqualTo("Field validation error");
        assertThat(errorDetails.getField()).isEqualTo("password");
        assertThat(errorDetails.getFieldMessage()).isEqualTo("password should be provided");
    }

    @Test
    void whenUsernameAndPasswordAreMissing_shouldReturn400Response() throws Exception {
        // Act
        var result = this.mockMvc.perform(post(CONTROLLER_MAPPING)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andReturn();


        // Assert
        var responseBody = result.getResponse().getContentAsString();
        var errorDetails = objectMapper.readValue(responseBody, ValidationErrorDetails.class);
        assertThat(errorDetails.getStatus()).isEqualTo(400);
        assertThat(errorDetails.getError()).isEqualTo("Field validation error");
        assertThat(errorDetails.getField()).isEqualTo("username, password");
        assertThat(errorDetails.getFieldMessage()).isEqualTo("username should be provided, password should be provided");
    }
}
