package org.example.catch_line.exception.phone;

public class InvalidPhoneNumberException extends RuntimeException {

    public InvalidPhoneNumberException() {
        super("올바른 전화번호 형식을 입력하세요.");
    }
}
