package org.example.crudspringboot.config.security;

import org.example.crudspringboot.model.User;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;

        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            errors.rejectValue("username", "username.empty", "Username cannot be empty");
        }

        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            errors.rejectValue("password", "password.empty", "Password cannot be empty");
        }

        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            errors.rejectValue("email", "email.empty", "Email cannot be empty");
        }
    }
}
