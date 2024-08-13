package org.example.catch_line.common.constant;

public enum Status {
	SCHEDULED("방문 예정"),
	COMPLETED("방문 완료"),
	CANCELED("취소");

	private final String description;

	Status(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
}
