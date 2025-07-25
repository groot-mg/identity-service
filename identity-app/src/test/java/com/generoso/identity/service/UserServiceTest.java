package com.generoso.identity.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.generoso.identity.exception.DownstreamException;
import com.generoso.identity.exception.RequestException;
import com.generoso.identity.service.validation.PasswordValidator;
import jakarta.ws.rs.InternalServerErrorException;
import jakarta.ws.rs.ProcessingException;
import org.jboss.resteasy.core.ServerResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UsersResource usersResource;

    @Mock
    private PasswordValidator passwordValidator;

    @InjectMocks
    private UserService service;

    @Test
    void shouldCreateAUserSuccessfully() {
        // Arrange
        var password = "password";
        var repeatPassword = "password";
        var user = new UserRepresentation();

        when(usersResource.create(user)).thenReturn(
                new ServerResponse(null, 201, null));

        // Act
        service.createUser(user, password, repeatPassword);

        // Assert
        verify(passwordValidator).validate(password, repeatPassword);
        verify(usersResource).create(user);
    }

    @Test
    void whenResponseGets404StatusCode_throwsRequestExceptionWithExpectedMessage() {
        // Arrange
        var password = "password";
        var repeatPassword = "password";
        var user = new UserRepresentation();

        when(usersResource.create(user)).thenReturn(
                new ServerResponse(null, 409, null));

        // Act
        var exception = assertThrows(RequestException.class,
                () -> service.createUser(user, password, repeatPassword));

        // Assert
        verify(passwordValidator).validate(password, repeatPassword);
        verify(usersResource).create(user);
        assertThat(exception.getMessage()).isEqualTo("Duplicated user");
    }

    @Test
    void whenResponseGets500StatusCode_throwsRequestExceptionWithExpectedMessage() {
        // Arrange
        var password = "password";
        var repeatPassword = "password";
        var user = new UserRepresentation();

        when(usersResource.create(user)).thenReturn(
                new ServerResponse(null, 500, null));

        // Act
        var exception = assertThrows(DownstreamException.class,
                () -> service.createUser(user, password, repeatPassword));

        // Assert
        verify(passwordValidator).validate(password, repeatPassword);
        verify(usersResource).create(user);
        assertThat(exception.getMessage()).isEqualTo("Downstream: KEYCLOAK");
    }

    @Test
    void shouldThrowsDownstreamExceptionWhenProcessingExceptionHappens() {
        // Arrange
        var password = "password";
        var repeatPassword = "password";
        var user = new UserRepresentation();

        when(usersResource.create(user)).thenThrow(new ProcessingException(""));

        // Act
        var exception = assertThrows(DownstreamException.class,
                () -> service.createUser(user, password, repeatPassword));

        // Assert
        verify(passwordValidator).validate(password, repeatPassword);
        verify(usersResource).create(user);
        assertThat(exception.getMessage()).isEqualTo("Downstream: KEYCLOAK");
    }

    @Test
    void shouldThrowsDownstreamExceptionWhenInternalServerErrorExceptionHappens() {
        // Arrange
        var password = "password";
        var repeatPassword = "password";
        var user = new UserRepresentation();

        when(usersResource.create(user)).thenThrow(new InternalServerErrorException(""));

        // Act
        var exception = assertThrows(DownstreamException.class,
                () -> service.createUser(user, password, repeatPassword));

        // Assert
        verify(passwordValidator).validate(password, repeatPassword);
        verify(usersResource).create(user);
        assertThat(exception.getMessage()).isEqualTo("Downstream: KEYCLOAK");
    }
}
