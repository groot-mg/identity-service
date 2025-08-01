package com.generoso.identity.exception;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import com.generoso.identity.exception.error.ErrorDetail;
import com.generoso.identity.exception.error.ValidationErrorDetails;
import com.generoso.identity.model.Downstream;
import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.core.Response;
import java.util.HashMap;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.MapBindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

class RestExceptionHandlerTest {

    private final RestExceptionHandler handler = new RestExceptionHandler();

    @Test
    @SuppressWarnings("ConstantConditions")
    void shouldReturnExpectedFieldsOnHandleExceptionInternal() {
        // arrange
        var exceptionMessage = "exceptionMessage";
        var exception = new Exception(exceptionMessage);
        var headers = new HttpHeaders();
        headers.add("Content-type", "application/json");
        var status = HttpStatus.INTERNAL_SERVER_ERROR;

        // Act
        var response = handler.handleExceptionInternal(exception, null, headers,
                status, null);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(status);
        assertThat(response.getHeaders()).isEqualTo(headers);

        var body = (ErrorDetail) response.getBody();
        assertThat(body.getStatus()).isEqualTo(status.value());
        assertThat(body.getError()).isEqualTo("Internal Exception");
        assertThat(body.getDetail()).isEqualTo(exceptionMessage);
    }

    @MethodSource("provideMessageValidationsToTest")
    @ParameterizedTest
    @SuppressWarnings("ConstantConditions")
    void shouldReturnExpectedFieldsOnHandleMethodArgumentNotValid(
            MapBindingResult bindingResult, String expectedField,
            String expectedMessage) {
        // arrange
        var parameter = mock(MethodParameter.class);
        var exception = new MethodArgumentNotValidException(parameter, bindingResult);

        var headers = new HttpHeaders();
        headers.add("Content-type", "application/json");
        var status = HttpStatus.BAD_REQUEST;

        // Act
        var response = handler.handleMethodArgumentNotValid(exception, headers, status, null);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(status);
        assertThat(response.getHeaders()).isEqualTo(headers);

        var body = (ValidationErrorDetails) response.getBody();
        assertThat(body.getStatus()).isEqualTo(status.value());
        assertThat(body.getError()).isEqualTo("Field validation error");
        assertThat(body.getDetail()).isNull();
        assertThat(body.getField()).isEqualTo(expectedField);
        assertThat(body.getFieldMessage()).isEqualTo(expectedMessage);
    }

    @Test
    @SuppressWarnings("ConstantConditions")
    void shouldReturnExpectedFieldsOnHandleNotAuthorizedException() {
        // arrange
        var exceptionMessage = "exceptionMessage";
        var exception = new NotAuthorizedException(exceptionMessage,
                Response.status(HttpStatus.UNAUTHORIZED.value()).build());
        var status = HttpStatus.UNAUTHORIZED;

        // Act
        var response = handler.handleNotAuthorizedException(exception);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(status);

        var body = response.getBody();
        assertThat(body.getStatus()).isEqualTo(status.value());
        assertThat(body.getError()).isEqualTo("Unauthorized");
        assertThat(body.getDetail()).isEqualTo(exceptionMessage);
    }

    @Test
    @SuppressWarnings("ConstantConditions")
    void shouldReturnExpectedFieldsOnHandleDownstreamException() {
        // arrange
        var exceptionMessage = "Downstream: KEYCLOAK";
        var exception = new DownstreamException(Downstream.KEYCLOAK);
        var status = HttpStatus.INTERNAL_SERVER_ERROR;

        // Act
        var response = handler.handleDownstreamException(exception);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(status);

        var body = response.getBody();
        assertThat(body.getStatus()).isEqualTo(status.value());
        assertThat(body.getError()).isEqualTo("Error connection to a downstream");
        assertThat(body.getDetail()).isEqualTo(exceptionMessage);
    }

    @Test
    @SuppressWarnings("ConstantConditions")
    void shouldReturnExpectedFieldsOnHandleValidationException() {
        // arrange
        var exceptionMessage = "validation message";
        var exception = new ValidationException(exceptionMessage);
        var status = HttpStatus.BAD_REQUEST;

        // Act
        var response = handler.handleValidationException(exception);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(status);

        var body = response.getBody();
        assertThat(body.getStatus()).isEqualTo(status.value());
        assertThat(body.getError()).isEqualTo("Validation error");
        assertThat(body.getDetail()).isEqualTo(exceptionMessage);
    }

    @Test
    @SuppressWarnings("ConstantConditions")
    void shouldReturnExpectedFieldsOnHandleRequestException() {
        // arrange
        var exceptionMessage = "request exception message";
        var expectedStatus = HttpStatus.UNAUTHORIZED;
        var exception = new RequestException(exceptionMessage, expectedStatus.value());

        // Act
        var response = handler.handleRequestException(exception);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(expectedStatus);

        var body = response.getBody();
        assertThat(body.getStatus()).isEqualTo(expectedStatus.value());
        assertThat(body.getError()).isEqualTo("Downstream request exception");
        assertThat(body.getDetail()).isEqualTo(exceptionMessage);
    }

    private static Stream<Arguments> provideMessageValidationsToTest() {
        var bindResult1 = new MapBindingResult(new HashMap<>(), "objectName");
        var error1 = new FieldError("objectName", "username", "must not be null");
        bindResult1.addError(error1);

        var bindResult2 = new MapBindingResult(new HashMap<>(), "objectName");
        var error2 = new FieldError("objectName", "username", "must not be null");
        var error3 = new FieldError("objectName", "password", "must not be null");
        bindResult2.addError(error2);
        bindResult2.addError(error3);

        return Stream.of(
                Arguments.of(bindResult1, "username", "username must not be null"),
                Arguments.of(bindResult2, "username, password", "username must not be null, password must not be null")
        );
    }
}
