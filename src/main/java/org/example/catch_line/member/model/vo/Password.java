package org.example.catch_line.member.model.vo;

import lombok.Getter;
import org.example.catch_line.exception.password.InvalidPasswordException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.regex.Pattern;

@Getter
public class Password {

    private final String passwordValue;

    private static final String PASSWORD_REGEX = "^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,15}$";

    private static final Pattern PASSWORD_PATTERN = Pattern.compile(PASSWORD_REGEX);


//    public Password(String passwordValue) {
//        validatePassword(passwordValue);
//        this.passwordValue = passwordValue;
//    }

    public Password(String passwordValue, boolean check) {
        if(!check){ // 원본 비밀번호만 검증한다.
            validatePassword(passwordValue);
        }
        this.passwordValue = passwordValue;
    }

//    public Password(String passwordValue, BCryptPasswordEncoder passwordEncoder) {
//        // 비밀번호 원문 검증
//        validatePassword(passwordValue);
//        // 비밀번호 암호화하여 저장
//        this.passwordValue = passwordEncoder.encode(passwordValue);
//    }

    private void validatePassword(String passwordValue) {

        if (!PASSWORD_PATTERN.matcher(passwordValue).matches()) {
            throw new InvalidPasswordException(passwordValue);
        }
    }
}
