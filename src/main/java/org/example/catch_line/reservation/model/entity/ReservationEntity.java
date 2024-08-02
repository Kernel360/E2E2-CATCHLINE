package org.example.catch_line.reservation.model.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import org.example.catch_line.common.BaseTimeEntity;
import org.example.catch_line.common.constant.Status;
import org.example.catch_line.member.model.entity.MemberEntity;
import org.example.catch_line.reservation.model.dto.ReservationRequest;
import org.example.catch_line.reservation.validation.ValidReservationDate;

import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.catch_line.restaurant.model.entity.RestaurantEntity;

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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private MemberEntity member;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "restaurant_id")
	private RestaurantEntity restaurant;

	@Builder
	public ReservationEntity(int memberCount, Status status, LocalDateTime reservationDate) {
		this.memberCount = memberCount;
		this.status = status;
		this.reservationDate = reservationDate;
	}

	public void updateReservation(ReservationRequest reservationRequest) {
		this.memberCount = reservationRequest.getMemberCount();
		this.reservationDate = reservationRequest.getReservationDate();
		this.status = reservationRequest.getStatus();
	}



}
