package org.example.catch_line.exception.booking;

public class WaitingException extends RuntimeException {
	
	public WaitingException() {
		super("이미 예약을 한 사용자입니다");
	}
}
