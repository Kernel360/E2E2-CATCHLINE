package org.example.catch_line.exception.service;

public class ServiceTypeException extends RuntimeException {

	public ServiceTypeException() {
		super("식당의 서비스타입이 일치하지 않습니다");
	}
}
