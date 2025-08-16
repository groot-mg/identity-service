package com.generoso.identity.api.converter;

import com.generoso.identity.api.dto.UserV1Dto;
import com.generoso.identity.model.UserType;
import org.junit.jupiter.api.Test;
import org.keycloak.representations.idm.UserRepresentation;

import static org.assertj.core.api.Assertions.assertThat;

class UserV1ConverterTest {

    private final UserV1Converter converter = new UserV1Converter();

    @Test
    void shouldConvertToEntityAsExpected() {
        // Arrange
        var userV1Dto = UserV1Dto.builder()
                .username("username")
                .firstName("firstName")
                .lastName("lastName")
                .email("email@email.com")
                .userType(UserType.CLIENT)
                .build();

        // Act
        var user = converter.convertToEntity(userV1Dto);

        // Assert
        assertThat(user.getUsername()).isEqualTo(userV1Dto.username());
        assertThat(user.getFirstName()).isEqualTo(userV1Dto.firstName());
        assertThat(user.getLastName()).isEqualTo(userV1Dto.lastName());
        assertThat(user.getEmail()).isEqualTo(userV1Dto.email());
        assertThat(user.getGroups()).contains(userV1Dto.userType().getGroupName());
    }

    @Test
    void shouldConvertToDtoAsExpected() {
        // Arrange
        var user = new UserRepresentation();
        user.setUsername("username");
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setEmail("email@email.com");

        // Act
        var userV1Dto = converter.convertToDto(user);

        // Assert
        assertThat(userV1Dto.username()).isEqualTo(user.getUsername());
        assertThat(userV1Dto.firstName()).isEqualTo(user.getFirstName());
        assertThat(userV1Dto.lastName()).isEqualTo(user.getLastName());
        assertThat(userV1Dto.email()).isEqualTo(user.getEmail());
    }
}
