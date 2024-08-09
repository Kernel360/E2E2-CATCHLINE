package org.example.catch_line.exception.booking;

import org.example.catch_line.exception.CatchLineException;

public class HistoryException extends CatchLineException {

	public HistoryException() {
		super("현재 booking 내역을 조회할 수 없습니다");
	}
}
