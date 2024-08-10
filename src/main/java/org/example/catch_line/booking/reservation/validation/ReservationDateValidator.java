package org.example.catch_line.booking.reservation.validation;

import java.time.LocalDateTime;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ReservationDateValidator implements ConstraintValidator<ValidReservationDate, LocalDateTime> {

	@Override
	public boolean isValid(LocalDateTime reservationDate, ConstraintValidatorContext context) {
		if (reservationDate == null) {
			return true;
		}
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime startDate = now.plusDays(0);
		LocalDateTime endDate = now.plusDays(8);

		return reservationDate.isAfter(startDate) && reservationDate.isBefore(endDate);
	}
}
