package com.generoso.identity.api.controller;

import com.generoso.identity.api.converter.UserV1Converter;
import com.generoso.identity.api.dto.UserV1Dto;
import com.generoso.identity.model.UserType;
import com.generoso.identity.service.UserService;
import com.generoso.identity.utils.JsonUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.stream.Stream;

import static com.generoso.identity.api.controller.CommonControllerTest.missingFieldsAssert400Response;
import static com.generoso.identity.utils.JsonUtils.asJsonString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserV1ControllerTest {

    private static final String CONTROLLER_MAPPING = "/v1/users";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService service;

    @SpyBean
    private UserV1Converter converter;

    @Test
    void whenCreateUserSuccessfully_shouldReturnSuccessResponse() throws Exception {
        // Arrange
        var userDto = UserV1Dto.builder()
                .username("username")
                .firstName("firstName")
                .lastName("lastName")
                .email("email@email.com")
                .password("password")
                .repeatPassword("password")
                .userType(UserType.CLIENT).build();


        // Act
        this.mockMvc.perform(post(CONTROLLER_MAPPING)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userDto)))
                .andExpect(status().isCreated())
                .andReturn();

        // Assert
        verify(service).createUser(any(UserRepresentation.class),
                eq(userDto.password()), eq(userDto.repeatPassword()));
    }

    @MethodSource("provideInputsToTestMissingField")
    @ParameterizedTest
    void whenFieldsAreMissing_shouldReturn400Response(String jsonBody, String field, String fieldMessage) throws Exception {
        missingFieldsAssert400Response(mockMvc, CONTROLLER_MAPPING, jsonBody, field, fieldMessage);
    }

    private static Stream<Arguments> provideInputsToTestMissingField() {
        return Stream.of(
                Arguments.of("""
                                {"email":"email@email.com","password":"password","repeatPassword":"password","firstName":"firstName","lastName":"lastName","userType":"CLIENT"}""",
                        "username",
                        "username must not be null"
                ),
                Arguments.of("""
                                {"username":"username","password":"password","repeatPassword":"password","firstName":"firstName","lastName":"lastName","userType":"CLIENT"}""",
                        "email",
                        "email must not be null"
                ),
                Arguments.of("""
                                {"username":"username","email": "email", "password":"password","repeatPassword":"password","firstName":"firstName","lastName":"lastName","userType":"CLIENT"}""",
                        "email",
                        "email must be a well-formed email address"
                ),
                Arguments.of("""
                                {"username":"username","email": "email@email.com","repeatPassword":"password","firstName":"firstName","lastName":"lastName","userType":"CLIENT"}""",
                        "password",
                        "password must not be null"
                ),
                Arguments.of("""
                                {"username":"username","email": "email@email.com", "password":"password","firstName":"firstName","lastName":"lastName","userType":"CLIENT"}""",
                        "repeatPassword",
                        "repeatPassword must not be null"
                ),
                Arguments.of("""
                                {"username":"username","email": "email@email.com", "password":"password","repeatPassword":"password","lastName":"lastName","userType":"CLIENT"}""",
                        "firstName",
                        "firstName must not be null"
                ),
                Arguments.of("""
                                {"username":"username","email": "email@email.com", "password":"password","repeatPassword":"password","firstName":"firstName","userType":"CLIENT"}""",
                        "lastName",
                        "lastName must not be null"
                ),
                Arguments.of("""
                                {"username":"username","email": "email@email.com", "password":"password","repeatPassword":"password","firstName":"firstName","lastName":"lastName"}""",
                        "userType",
                        "userType must not be null"
                )
        );
    }
}
