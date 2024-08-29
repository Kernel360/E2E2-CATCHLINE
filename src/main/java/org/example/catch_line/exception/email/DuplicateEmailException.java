package org.example.catch_line.exception.email;


import org.example.catch_line.exception.CatchLineException;

public class DuplicateEmailException extends CatchLineException {

    public DuplicateEmailException(String email) {
        super("이미 존재하는 이메일입니다. : " + email);
    }
}
