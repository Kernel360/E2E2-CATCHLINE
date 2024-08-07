package org.example.catch_line.exception.email;

public class InvalidEmailException extends RuntimeException {

    public InvalidEmailException() {
        super("올바른 이메일 형식을 입력하세요.");
    }
}
