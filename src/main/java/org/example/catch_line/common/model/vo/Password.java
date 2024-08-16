package org.example.catch_line.common.model.vo;

import lombok.Getter;
import org.example.catch_line.exception.password.InvalidEncodedPasswordException;

import java.util.regex.Pattern;

// VO 객체의 역할 중 검증 후 객체 생성 부분이 없어지면서 VO의 의미를 잃어버린 것 같았다.
// TODO: 그래서 암호화가 된 패스워드인지를 확인하는 검증을 추가했습니다.
@Getter
public class Password {

    private final String encodedPassword;


    private static final Pattern BCRYPT_PATTERN = Pattern.compile("^\\$2[aby]\\$\\d{2}\\$[./A-Za-z0-9]{22}[./A-Za-z0-9]{31}$");


    public Password(String password) {
        validateBCryptPassword(password);
        this.encodedPassword = password;
    }

    private void validateBCryptPassword(String password) {

        if (!BCRYPT_PATTERN.matcher(password).matches()) throw new InvalidEncodedPasswordException(password);
    }


}
