package org.example.catch_line.exception.phone;

import org.example.catch_line.exception.CatchLineException;

public class InvalidPhoneNumberException extends CatchLineException {

    public InvalidPhoneNumberException(String phoneNumber) {
        super("올바른 전화번호 형식을 입력하세요. (010-0000-0000): " + phoneNumber);
    }
}
