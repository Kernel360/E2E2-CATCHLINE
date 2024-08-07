package org.example.catch_line.exception.email;

import org.example.catch_line.exception.CatchLineException;

public class InvalidEmailException extends CatchLineException {

    public InvalidEmailException(String email) {
        super("올바른 이메일 형식을 입력하세요. : " + email);
    }
}
