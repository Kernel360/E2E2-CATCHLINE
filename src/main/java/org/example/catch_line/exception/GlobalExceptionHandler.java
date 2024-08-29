package org.example.catch_line.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
// TODO: RestControllerAdvice vs ControllerAdvice 어노테이션
@RestControllerAdvice
@Order(value = Integer.MAX_VALUE) // 처리되지 않은 예외들은 마지막에 여기로 모임
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        log.error("IllegalArgumentException 발생: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }

    // 모든 커스텀 예외가 `CatchLineException`을 상속 -> 모든 `exception class` 다 적을 필요 없음.
    @ExceptionHandler(CatchLineException.class)
    public ResponseEntity<String> handleCatchLineException(CatchLineException e) {
        log.error("캐치라인 커스텀 예외 발생: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        log.error("Exception 예외 발생: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }

}
