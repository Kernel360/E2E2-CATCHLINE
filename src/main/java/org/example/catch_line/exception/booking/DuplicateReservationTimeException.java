package org.example.catch_line.exception.booking;

import org.example.catch_line.exception.CatchLineException;

public class DuplicateReservationTimeException extends CatchLineException {

    private static final String message = "해당 시간에 예약이 존재합니다";

    public DuplicateReservationTimeException() {
        super(message);
    }
}
