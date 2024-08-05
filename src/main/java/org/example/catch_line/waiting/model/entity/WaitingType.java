package org.example.catch_line.waiting.model.entity;

public enum WaitingType {
	DINE_IN("매장"),
	TAKE_OUT("포장");

	private final String description;

	WaitingType(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
}
