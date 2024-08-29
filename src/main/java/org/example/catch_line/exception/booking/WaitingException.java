package org.example.catch_line.exception.booking;

import org.example.catch_line.exception.CatchLineException;

public class WaitingException extends CatchLineException {

	public WaitingException(String message) {
		super(message);
	}
}
