package com.generoso.identity.api.converter;

import com.generoso.identity.api.dto.UserV1Dto;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Component;

@Component
public class UserV1Converter {

    public UserRepresentation convertToEntity(UserV1Dto userV1Dto) {
        var user = new UserRepresentation();
        user.setUsername(userV1Dto.username());
        user.setFirstName(userV1Dto.firstName());
        user.setLastName(userV1Dto.lastName());
        user.setEmail(userV1Dto.emailId());
        return user;
    }

    public UserV1Dto convertToDto(UserRepresentation user) {
        return new UserV1Dto(
                user.getUsername(),
                user.getEmail(),
                null,
                user.getFirstName(),
                user.getLastName()
        );
    }
}
