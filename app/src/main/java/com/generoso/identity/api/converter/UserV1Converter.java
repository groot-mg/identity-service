package com.generoso.identity.api.converter;

import com.generoso.identity.api.dto.UserV1Dto;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class UserV1Converter {

    public UserRepresentation convertToEntity(UserV1Dto userV1Dto) {
        var user = new UserRepresentation();
        user.setUsername(userV1Dto.username());
        user.setFirstName(userV1Dto.firstName());
        user.setLastName(userV1Dto.lastName());
        user.setEmail(userV1Dto.email());
        user.setGroups(Collections.singletonList(userV1Dto.userType().getGroupName()));
        return user;
    }

    public UserV1Dto convertToDto(UserRepresentation user) {
        return UserV1Dto.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName()).build();
    }
}
