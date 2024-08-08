package org.example.catch_line.exception.password;

import org.example.catch_line.exception.CatchLineException;

public class InvalidEncodedPasswordException extends CatchLineException {


    public InvalidEncodedPasswordException(String encodedPassword) {
        super("암호화되지 않은 패스워드입니다. : " + encodedPassword);
    }

}
