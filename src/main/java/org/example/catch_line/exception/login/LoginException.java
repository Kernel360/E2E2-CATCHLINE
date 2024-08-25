package org.example.catch_line.exception.login;

import org.example.catch_line.exception.CatchLineException;

public class LoginException extends CatchLineException {
    public LoginException(String message) {
        super("존재하지 않는 이메일이거나, 잘못된 비밀번호를 입력했습니다. 다시 로그인해주세요.");
    }
}
