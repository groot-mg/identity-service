package com.generoso.identity.service.validation;

import com.generoso.identity.exception.ValidationException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PasswordValidatorTest {

    private final PasswordValidator validator = new PasswordValidator();

    @Test
    void shouldPassTheValidationSuccessfullyWhenPasswordsMatch() {
        validator.validate("password", "password");
    }

    @Test
    void shouldThrowsValidationExceptionWhenPasswordsDontMatch() {
        var exception = assertThrows(ValidationException.class,
                () -> validator.validate("password", "password1"));

        assertThat(exception.getMessage()).isEqualTo("Passwords don't match");
    }
}
