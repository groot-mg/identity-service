package com.generoso.identity.service;

import com.generoso.identity.exception.RequestException;
import com.generoso.identity.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class KeyCloakService {

    private final UsersResource usersResource;

    public void createUser(UserRepresentation user, String password) {
        var credential = createPasswordCredentials(password);
        user.setCredentials(Collections.singletonList(credential));
        user.setEnabled(true);
        try (var response = usersResource.create(user)) {
            validateKeycloakResponse(response);
        }
    }

    public UserRepresentation getUser(String username) {
        var users = usersResource.search(username, true);
        if (users.isEmpty()) {
            throw new ResourceNotFoundException(format("User %s not found", username));
        }

        return users.get(0);
    }

    public void updateUser(String userId, UserRepresentation user) {
        var existingUser = getUser(user.getUsername());
        existingUser.setUsername(user.getUsername());
        existingUser.setFirstName(user.getFirstName());
        existingUser.setLastName(user.getLastName());
        existingUser.setEmail(user.getEmail());
        usersResource.get(userId).update(existingUser);
    }

    public void deleteUser(String userId) {
        usersResource.get(userId).remove();
    }


    public void sendVerificationLink(String userId) {
        usersResource.get(userId)
                .sendVerifyEmail();
    }

    public void sendResetPassword(String userId) {
        usersResource.get(userId).executeActionsEmail(List.of("UPDATE_PASSWORD"));
    }

    private CredentialRepresentation createPasswordCredentials(String password) {
        var passwordCredentials = new CredentialRepresentation();
        passwordCredentials.setTemporary(false);
        passwordCredentials.setType(CredentialRepresentation.PASSWORD);
        passwordCredentials.setValue(password);
        return passwordCredentials;
    }

    private void validateKeycloakResponse(Response response) {
        if (response.getStatus() >= 200 && response.getStatus() <= 204) {
            return;
        }

        throw new RequestException(response.readEntity(String.class), response.getStatus());
    }
}
