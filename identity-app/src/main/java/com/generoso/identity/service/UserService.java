package com.generoso.identity.service;

import com.generoso.identity.exception.DownstreamException;
import com.generoso.identity.exception.RequestException;
import com.generoso.identity.model.Downstream;
import com.generoso.identity.service.validation.PasswordValidator;
import jakarta.ws.rs.InternalServerErrorException;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserService {

    private final UsersResource usersResource;
    private final PasswordValidator passwordValidator;

    public void createUser(UserRepresentation user, String password, String repeatPassword) {
        passwordValidator.validate(password, repeatPassword);

        var credential = createPasswordCredentials(password);
        user.setCredentials(Collections.singletonList(credential));
        user.setEnabled(true);
        try (var response = usersResource.create(user)) {
            validateKeycloakResponse(response, user);
        } catch (ProcessingException | InternalServerErrorException ex) {
            log.error("Error sending request to create a new user: {}", ex.getMessage());
            throw new DownstreamException(Downstream.KEYCLOAK);
        }
    }

    private CredentialRepresentation createPasswordCredentials(String password) {
        var passwordCredentials = new CredentialRepresentation();
        passwordCredentials.setTemporary(false);
        passwordCredentials.setType(CredentialRepresentation.PASSWORD);
        passwordCredentials.setValue(password);
        return passwordCredentials;
    }

    private void validateKeycloakResponse(Response response, UserRepresentation user) {
        switch (response.getStatus()) {
            case 201 -> log.info("Created user {}", user.getUsername());
            case 409 -> throw new RequestException("Duplicated user", HttpStatus.CONFLICT.value());
            default -> {
                log.error("Unexpected error creating user: {}", response.getEntity());
                throw new DownstreamException(Downstream.KEYCLOAK);
            }
        }
    }
}
