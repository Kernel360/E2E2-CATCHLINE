package org.example.catch_line.exception.password;

import org.example.catch_line.exception.CatchLineException;

public class InvalidPasswordException extends CatchLineException {
    public InvalidPasswordException(String password) {
        super("올바른 패스워드 형식을 입력하세요. : " + password);
    }
}