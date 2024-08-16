package org.example.catch_line.common.model.vo;

import lombok.Getter;
import org.example.catch_line.exception.email.InvalidEmailException;

import java.util.regex.Pattern;

// VO 클래스는 불변성 보장 -> 왜? 이유를 명확하게 모르겠음.
// TODO: @Value 어노테이션이 필요하다면?? (private final)
@Getter
public class Email {

    private final String emailValue;


    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);


    // VO 객체가 생성될 때 검증까지 마침 -> 검증되지 않으면 객체 생성 불가
    public Email(String emailValue) {
        validateEmail(emailValue);
        this.emailValue = emailValue;
    }

    // 이메일에 대한 모든 검증을 여기서 ??
    private void validateEmail(String emailValue) {

        // TODO: null 체크를 dto에서 해야 할지 여기서 해야 할지? validation 책임 분리
        if (!EMAIL_PATTERN.matcher(emailValue).matches()) {
            throw new InvalidEmailException(emailValue);
        }
    }

}
