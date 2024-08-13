package org.example.catch_line.exception.booking;

import org.example.catch_line.exception.CatchLineException;

public class BookingErrorException extends CatchLineException {
	public BookingErrorException() {
		super("해당 Booking 은 존재하지 않습니다");
	}
}
