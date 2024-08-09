package org.example.catch_line.exception.booking;

import org.example.catch_line.exception.CatchLineException;

public class ServiceIdException extends CatchLineException {
	public ServiceIdException() {
		super("올바른 경로로 접속해주세요");
	}
}
