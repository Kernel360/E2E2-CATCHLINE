package org.example.catch_line.exception.service;

public class ServiceIdException extends RuntimeException{
	public ServiceIdException() {
		super("올바른 경로로 접속해주세요");
	}
}
