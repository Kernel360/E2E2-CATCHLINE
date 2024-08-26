package org.example.catch_line.exception.authorizaion;

import org.example.catch_line.exception.CatchLineException;

public class UnauthorizedException extends CatchLineException {

    private static final String MESSAGE = "해당 작업에 대한 권한이 없습니다.";

    public UnauthorizedException() {
        super(MESSAGE);
    }
}
