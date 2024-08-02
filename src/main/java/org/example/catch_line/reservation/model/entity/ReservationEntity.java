package org.example.catch_line.reservation.model.entity;

import java.time.LocalDateTime;

import org.example.catch_line.common.BaseTimeEntity;
import org.example.catch_line.common.constant.Status;
import org.example.catch_line.reservation.validation.ValidReservationDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "reservation")
public class ReservationEntity extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
	private Long reservationId;

	@Column(nullable = false)
	@Min(value = 1, message = "최소 인원 수는 1명입니다")
	private int memberCount;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Status status;

	@ValidReservationDate
	@Column(nullable = false)
	private LocalDateTime reservationDate;

	@Builder
	public ReservationEntity(int memberCount, Status status, LocalDateTime reservationDate) {
		this.memberCount = memberCount;
		this.status = status;
		this.reservationDate = reservationDate;
	}

}
