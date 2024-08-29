package org.example.catch_line.user.member.model.provider.validation;

import org.example.catch_line.exception.password.InvalidPasswordException;

import java.util.regex.Pattern;

public class PasswordValidator {
    private static final String PASSWORD_REGEX = "^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,15}$";
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(PASSWORD_REGEX);

    // TODO: static vs @Component vs new 로 생성 .. ?
    public static String validatePassword(String password) {

        if (!PASSWORD_PATTERN.matcher(password).matches()) {
            throw new InvalidPasswordException(password);
        }
        return password;
    }
}
