package org.example.catch_line.exception.booking;

import org.example.catch_line.exception.CatchLineException;

public class ReservationException extends CatchLineException {

    public ReservationException(String message) {
        super(message);
    }
}
