package org.example.catch_line.exception;

public class CatchLineException extends RuntimeException {

    // Custom Exception의 최상위 Exception으로 이 Exception을 이용하고 있습니다.
    // 다른 Custom Exception은 모두 이 Exception을 상속받고 있습니다.
    // Exception에 추가로 넣어야 할 정보와 실무에서는 Custom Exception을 어떻게 사용하고 계신지 궁금합니다.
    public CatchLineException(String message) {
        super(message);
    }
}


