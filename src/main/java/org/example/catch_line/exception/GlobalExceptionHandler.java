package org.example.catch_line.exception;

import lombok.extern.slf4j.Slf4j;
import org.example.catch_line.exception.email.DuplicateEmailException;
import org.example.catch_line.exception.email.InvalidEmailException;
import org.example.catch_line.exception.phone.InvalidPhoneNumberException;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
@Order(value = Integer.MAX_VALUE)
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {

        log.error("IllegalArgumentException 발생: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }


    @ExceptionHandler({DuplicateEmailException.class, InvalidEmailException.class, InvalidPhoneNumberException.class})
    public ResponseEntity<String> handleIllegalArgumentException(RuntimeException e) {

        log.error("커스텀 예외 발생: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }


}
