package org.example.catch_line.exception.booking;

import org.example.catch_line.exception.CatchLineException;

public class WaitingException extends CatchLineException {

	public WaitingException() {
		super("웨이팅 등록이 되어있습니다");
	}
}
