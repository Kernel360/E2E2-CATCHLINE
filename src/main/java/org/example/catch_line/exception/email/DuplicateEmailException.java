package org.example.catch_line.exception.email;


// TODO: 더 구체적인 예외를 상속해야 할까요?
public class DuplicateEmailException extends RuntimeException {

    public DuplicateEmailException() {
        super("이미 존재하는 이메일입니다.");
    }
}
