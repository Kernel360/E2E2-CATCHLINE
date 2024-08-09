package org.example.catch_line.exception.booking;

public class HistoryException extends RuntimeException{

	public HistoryException() {
		super("현재 booking내역을 조회할 수 없습니다");
	}
}
