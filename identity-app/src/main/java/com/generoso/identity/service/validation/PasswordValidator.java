package com.generoso.identity.service.validation;

import com.generoso.identity.exception.ValidationException;
import org.springframework.stereotype.Component;

@Component
public class PasswordValidator {

    public void validate(String password, String repeatPassword) {
        if (password.equals(repeatPassword)) {
            return;
        }

        throw new ValidationException("Passwords don't match");
    }
}
