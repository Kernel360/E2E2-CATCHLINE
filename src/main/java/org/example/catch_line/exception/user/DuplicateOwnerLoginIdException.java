package org.example.catch_line.exception.user;

import org.example.catch_line.exception.CatchLineException;

public class DuplicateOwnerLoginIdException extends CatchLineException {

    public DuplicateOwnerLoginIdException(String message) {
        super("이미 존재하는 식당 사장님 로그인 아이디입니다 : " + message);
    }
}
