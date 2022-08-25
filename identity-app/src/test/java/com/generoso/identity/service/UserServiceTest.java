package com.generoso.identity.service;

import com.generoso.identity.exception.DownstreamException;
import com.generoso.identity.service.validation.PasswordValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.ws.rs.ProcessingException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

        // Act
        service.createUser(user, password, repeatPassword);

        // Assert
        verify(passwordValidator).validate(password, repeatPassword);
        verify(usersResource).create(user);
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
        assertThat(exception.getMessage()).isEqualTo("Downstream down: KEYCLOAK");
    }
}
