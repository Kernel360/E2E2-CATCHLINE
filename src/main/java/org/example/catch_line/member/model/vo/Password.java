package org.example.catch_line.member.model.vo;

import lombok.Getter;
import org.example.catch_line.exception.password.InvalidPasswordException;

import java.util.regex.Pattern;

@Getter
public class Password {

    private final String passwordValue;

    private static final String PASSWORD_REGEX = "^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,15}$";

    private static final Pattern PASSWORD_PATTERN = Pattern.compile(PASSWORD_REGEX);


    public Password(String passwordValue) {
        validatePassword(passwordValue);
        this.passwordValue = passwordValue;
    }

    private void validatePassword(String passwordValue) {

        if (!PASSWORD_PATTERN.matcher(passwordValue).matches()) {
            throw new InvalidPasswordException(passwordValue);
        }
    }
}
