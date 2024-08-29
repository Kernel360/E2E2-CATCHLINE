package org.example.catch_line.exception.user;

import org.example.catch_line.exception.CatchLineException;

public class MemberNotFoundException extends CatchLineException {

    public MemberNotFoundException(String message) {
        super(message);
    }

    public MemberNotFoundException(Long memberId) {
        super("해당하는 사용자가 없습니다 : " + memberId);
    }
}
