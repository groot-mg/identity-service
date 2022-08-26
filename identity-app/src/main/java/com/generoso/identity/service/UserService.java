package com.generoso.identity.service;

import com.generoso.identity.exception.DownstreamException;
import com.generoso.identity.model.Downstream;
import com.generoso.identity.service.validation.PasswordValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.ProcessingException;
import java.util.Collections;

@Service
@Slf4j
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
            log.info("Created user {}", user.getUsername());
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
}
