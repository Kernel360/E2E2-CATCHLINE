package org.example.catch_line.exception.session;

import org.example.catch_line.exception.CatchLineException;

public class InvalidSessionException extends CatchLineException {


    public InvalidSessionException() {
        super("로그인이 필요합니다.");
    }
}
